package puppeteer.annotation.processor;

import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;
import piutils.map.SetMap;
import puppeteer.Puppeteer;
import puppeteer.annotation.type.AnnotationType;
import puppeteer.util.PackageUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Set;

public class AnnotationProcessor {

    private final Reflections reflections;
    private final SetMap<AnnotationType, Class<? extends Annotation>> annotationMap;
    private final ClassInstanceHandler classInstanceHandler;

    public AnnotationProcessor(final Puppeteer puppeteer,
                               final SetMap<AnnotationType, Class<? extends Annotation>> annotationMap,
                               final Collection<String> libraries,
                               final Collection<String> packages) {
        this.annotationMap = annotationMap;
        this.reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(
                        new FieldAnnotationsScanner(),
                        new TypeAnnotationsScanner(),
                        new SubTypesScanner(),
                        new MethodAnnotationsScanner())
                .setUrls(PackageUtils.getUrlsOfPackages(PackageUtils.findAllMatchingPackages(libraries, packages))));
        this.classInstanceHandler = new ClassInstanceHandler(puppeteer, reflections, annotationMap);

    }

    public void processAnnotations() throws InstantiationException, IllegalAccessException, InvocationTargetException {
        processFieldAnnotations(annotationMap, AnnotationType.FIELD);
    }

    public Object getInstanceOf(Class clazz) {
        return classInstanceHandler.getInstanceOf(clazz);
    }

    private void processFieldAnnotations(final SetMap<AnnotationType, Class<? extends Annotation>> annotationMap, final AnnotationType type) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        for (Class<? extends Annotation> annotation : annotationMap.get(type)) {
            Set<Field> fields = reflections.getFieldsAnnotatedWith(annotation);
            for (Field field : fields) {
                field.setAccessible(true);

                Class declaringClass = field.getDeclaringClass();
                Class fieldType = field.getType();

                if (Modifier.isStatic(field.getModifiers())) {
                    field.set(declaringClass, classInstanceHandler.createClassInstanceOf(fieldType, field.getGenericType()));
                } else {
                    field.set(classInstanceHandler.createClassInstanceOf(declaringClass), classInstanceHandler.createClassInstanceOf(fieldType, field.getGenericType()));
                }
            }
        }
    }

}
