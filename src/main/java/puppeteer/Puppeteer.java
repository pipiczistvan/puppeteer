package puppeteer;

import piutils.map.SetMap;
import puppeteer.annotation.premade.Component;
import puppeteer.annotation.premade.Wire;
import puppeteer.annotation.processor.AnnotationProcessor;
import puppeteer.annotation.type.AnnotationType;
import puppeteer.exception.PuppeteerException;

import java.lang.annotation.Annotation;

@SuppressWarnings("unchecked")
@Component
public class Puppeteer {

    private final SetMap<AnnotationType, Class<? extends Annotation>> annotationMap;
    private final AnnotationProcessor annotationProcessor;
    private final String[] packages;

    /***
     * The entry point of the Puppeteer Framework.
     * @param prefix The prefix of the packages. The framework will only look for packages, that start with this.
     * @param packages The framework will search for annotated things in these packages. It accepts regexps too.
     */
    public Puppeteer(final String prefix, final String... packages) {
        this.packages = packages;
        this.annotationMap = new SetMap<>();
        this.annotationProcessor = new AnnotationProcessor(prefix);
    }

    /***
     * It starts annotation processing. Searches for field, and static field annotations, and creates class instances, if necessary.
     * @throws PuppeteerException A general exception of the framework.
     */
    public void processAnnotations() throws PuppeteerException {
        try {
            annotationProcessor.processAnnotations(this, annotationMap, packages);
        } catch (Exception e) {
            throw new PuppeteerException(e.getMessage());
        }
    }

    /***
     * Use the predefined annotations:
     * Component(unique=true) - Type annotation
     * Wire - Field, and constructor annotation
     */
    public void useDefaultAnnotations() {
        addAnnotation(AnnotationType.TYPE, Component.class);
        addAnnotation(AnnotationType.CONSTRUCTOR, Wire.class);
        addAnnotation(AnnotationType.FIELD, Wire.class);
    }

    /***
     * Specifies which annotations the framework should care.
     * @param type The type of the annotation:
     *             <p>Field: The declaring class must be annotated, and the field must not be static.</p>
     *             <p>Static field: The declaring class does not have to be annotated, but the field must be static.</p>
     *             <p>Type: All injected instance will be unique.</p>
     *             <p>Static type: All injected instance will be the same.</p>
     *             <p>Constructor: All parameters will be injected.</p>
     * @param annotation The annotation class.
     */
    public void addAnnotation(final AnnotationType type, final Class<? extends Annotation> annotation) {
        annotationMap.put(type, annotation);
    }

    /***
     * Returns a new instance of the class, and injects the required parameters.
     * @param clazz The type
     * @return The class instance.
     */
    public <T> T getInstanceOf(Class<T> clazz) {
        return (T) annotationProcessor.getInstanceOf(clazz);
    }

}
