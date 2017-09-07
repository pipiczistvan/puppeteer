package puppeteer.annotation.processor;

import org.reflections.Reflections;
import piutils.array.ArrayUtils;
import piutils.map.SetMap;
import puppeteer.Puppeteer;
import puppeteer.annotation.premade.Unique;
import puppeteer.annotation.type.AnnotationType;
import puppeteer.exception.PuppeteerException;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class ClassInstanceHandler {

    private final Reflections reflections;
    private final SetMap<AnnotationType, Class<? extends Annotation>> annotationMap;
    private final Map<Class, Object> objectMap;

    ClassInstanceHandler(final Puppeteer puppeteer, final Reflections reflections, final SetMap<AnnotationType, Class<? extends Annotation>> annotationMap) {
        this.reflections = reflections;
        this.annotationMap = annotationMap;
        this.objectMap = new HashMap<>();

        this.objectMap.put(Puppeteer.class, puppeteer);
    }

    Object createClassInstanceOf(final Class classType, final Type parameterizedType) {
        Type genericType = classType;

        boolean isList = false;
        if (classType.isAssignableFrom(List.class)) {
            isList = true;
            genericType = ((ParameterizedType) parameterizedType).getActualTypeArguments()[0];
        }

        Set<Class> suitableClasses = getSuitableClassesOf(genericType);

        List<Object> objects = new ArrayList<>();

        for (Class c : suitableClasses) {
            Object o = createClassInstanceOf(c);
            if (o != null) {
                objects.add(o);
            }
        }

        if (isList) {
            return objects;
        } else {
            if (objects.size() > 1) {
                throw new PuppeteerException("Found too many annotated suitable classes " + objects + " for class: " + genericType.getTypeName());
            }
            if (objects.size() < 1) {
                throw new PuppeteerException("Could not find annotated suitable classes for class: " + genericType.getTypeName());
            }
            return objects.get(0);
        }
    }

    Object createClassInstanceOf(final Class clazz) {
        Class<? extends Annotation> annotation = getTypeAnnotationOfClass(clazz);

        if (annotation != null) {
            if (clazz.isAnnotationPresent(Unique.class)) {
                return createInstanceOf(clazz);
            } else {
                return getInstanceOf(clazz);
            }
        }

        return null;
    }

    Object getInstanceOf(final Class clazz) {
        Object object = objectMap.get(clazz);
        if (object == null) {
            object = createInstanceOf(clazz);
            objectMap.put(clazz, object);
        }

        return object;
    }

    private Object createInstanceOf(final Class clazz) {
        try {
            Constructor classConstructor = getAnnotatedConstructorOf(clazz);
            if (classConstructor != null) {
                classConstructor.setAccessible(true);
                Parameter[] parameters = classConstructor.getParameters();
                Object[] parameterObjects = new Object[parameters.length];
                for (int i = 0; i < parameters.length; i++) {
                    parameterObjects[i] = createClassInstanceOf(parameters[i].getType(), parameters[i].getParameterizedType());
                }

                return classConstructor.newInstance(parameterObjects);
            }
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new PuppeteerException("Could not create new instance of class: " + clazz.getName());
        }
    }

    private Set<Class> getSuitableClassesOf(final Type type) {
        Set<Class> suitableClasses = new HashSet<>();

        Set<Class<? extends Annotation>> annotationClasses = annotationMap.get(AnnotationType.TYPE);

        Set<Class> annotatedClasses = new HashSet<>();
        annotatedClasses.add(Puppeteer.class);
        for (Class<? extends Annotation> annotation : annotationClasses) {
            annotatedClasses.addAll(reflections.getTypesAnnotatedWith(annotation));
        }

        if (type instanceof ParameterizedType) {
            Type[] typeArguments = ((ParameterizedType) type).getActualTypeArguments();

            suitableClasses.addAll(annotatedClasses.stream()
                    .filter(c -> ((ParameterizedTypeImpl) type).getRawType().isAssignableFrom(c) && c.getGenericSuperclass() instanceof ParameterizedType)
                    .filter(c -> ArrayUtils.equals(typeArguments, ((ParameterizedType) c.getGenericSuperclass()).getActualTypeArguments()))
                    .collect(Collectors.toList()));
        } else if (type instanceof Class) {
            suitableClasses.addAll(annotatedClasses.stream()
                    .filter(((Class) type)::isAssignableFrom)
                    .collect(Collectors.toList()));
        }

        return suitableClasses;
    }

    private Constructor getAnnotatedConstructorOf(final Class clazz) {
        for (Constructor constructor : clazz.getConstructors()) {
            for (Class<? extends Annotation> annotation : annotationMap.get(AnnotationType.CONSTRUCTOR)) {
                if (constructor.isAnnotationPresent(annotation)) {
                    return constructor;
                }
            }
        }
        return null;
    }

    private Class<? extends Annotation> getTypeAnnotationOfClass(final Class clazz) {
        for (Class<? extends Annotation> annotation : annotationMap.get(AnnotationType.TYPE)) {
            if (clazz.isAnnotationPresent(annotation)) {
                return annotation;
            }
        }

        return null;
    }

}
