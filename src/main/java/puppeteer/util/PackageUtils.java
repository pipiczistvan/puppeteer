package puppeteer.util;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PackageUtils {

    /***
     * Returns all matching packages from the libraries.
     * @param libraries The libraries regex.
     * @param packages The packages regex.
     * @return A set of string, which contains the names of the packages.
     */
    public static Set<String> findAllMatchingPackages(Collection<String> libraries, Collection<String> packages) {
        List<Pattern> libraryPatterns = compilePatterns(libraries);
        List<Pattern> packagePatterns = compilePatterns(packages);

        Set<Class<?>> classes = collectClassesFromLibraries(libraryPatterns);

        return classes.stream()
                .map(classInstance -> classInstance.getPackage().getName())
                .filter(packageName -> matchesAnyPattern(packageName, packagePatterns))
                .collect(Collectors.toCollection(HashSet::new));
    }

    /***
     * Returns all the urls of the given packages.
     * @param packages The collection of package names.
     * @return A set of Url for the packages.
     */
    public static Set<URL> getUrlsOfPackages(Collection<String> packages) {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        return packages.stream()
                .map(pkg -> classLoader.getResource(pkg.replace('.', '/')))
                .collect(Collectors.toSet());
    }

    private static Set<Class<?>> collectClassesFromLibraries(Collection<Pattern> libraryPatterns) {
        Collection<URL> urlCollection = getUrlCollection(libraryPatterns);

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false), new ResourcesScanner())
                .setUrls(urlCollection));

        return reflections.getSubTypesOf(Object.class);
    }

    private static Collection<URL> getUrlCollection(Collection<Pattern> libraryPatterns) {
        return ClasspathHelper.forClassLoader()
                .stream()
                .filter(url -> matchesAnyPattern(url.getFile(), libraryPatterns))
                .collect(Collectors.toSet());
    }

    private static boolean matchesAnyPattern(String input, Collection<Pattern> patterns) {
        for (Pattern pattern : patterns) {
            if (pattern.matcher(input).matches()) {
                return true;
            }
        }
        return false;
    }

    private static List<Pattern> compilePatterns(Collection<String> regExps) {
        return regExps.stream()
                .map(Pattern::compile)
                .collect(Collectors.toList());
    }

}
