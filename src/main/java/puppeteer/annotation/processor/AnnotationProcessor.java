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
import java.util.Set;

public class AnnotationProcessor {

    private final String prefix;

    private Reflections reflections;
    private ClassInstanceHandler classInstanceHandler;

    public AnnotationProcessor(final String prefix) {
        this.prefix = prefix;
    }

    public void processAnnotations(final Puppeteer puppeteer, final SetMap<AnnotationType, Class<? extends Annotation>> annotationMap, final String... packageRegexps) throws InstantiationException, IllegalAccessException, InvocationTargetException {
        this.reflections = new Reflections(new ConfigurationBuilder()
                        .setScanners(
                                new FieldAnnotationsScanner(),
                                new TypeAnnotationsScanner(),
                                new SubTypesScanner(),
                                new MethodAnnotationsScanner())
                        .setUrls(PackageUtils.getUrlsFromPackageRegExps(PackageUtils.findAllPackagesStartingWith(prefix), packageRegexps)));

        this.classInstanceHandler = new ClassInstanceHandler(puppeteer, reflections, annotationMap);

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
                }
                else {
                    field.set(classInstanceHandler.createClassInstanceOf(declaringClass), classInstanceHandler.createClassInstanceOf(fieldType, field.getGenericType()));
                }
            }
        }
    }

}
