package puppeteer.util;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.net.URL;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PackageUtils {

    private static Collection<URL> URL_COLLECTION = getUrls();

    /***
     * Returns all packages, which matches with the given prefix.
     * @param prefix The prefix.
     * @return A set of string, which contains the names of the packages.
     */
    public static Set<String> findAllPackagesStartingWith(String prefix) {

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false), new ResourcesScanner())
                .setUrls(URL_COLLECTION)
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(prefix))));
        Set<Class<?>> classes = reflections.getSubTypesOf(Object.class);

        return classes.stream().map(classInstance -> classInstance.getPackage().getName()).collect(Collectors.toCollection(TreeSet::new));
    }

    /***
     * Returns all Urls, which matches with the given regular expressions.
     * @param packages The collection of package names, to search in.
     * @param regExps The array of regular expressions.
     * @return A set of Url for the packages.
     */
    public static Set<URL> getUrlsFromPackageRegExps(Collection<String> packages, String[] regExps) {
        Set<URL> urlSet = new HashSet<>();
        for (String regExp : regExps) {
            urlSet.addAll(getUrlsFromPackageRegExp(packages, regExp));
        }

        return urlSet;
    }

    private static Collection<URL> getUrls() {
        return ClasspathHelper.forResource("");
    }

    private static Set<URL> getUrlsFromPackageRegExp(Collection<String> packages, String regexp) {
        Pattern pattern = Pattern.compile(regexp);
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();

        Set<URL> urlSet = new HashSet<>();
        for (String p : packages) {
            Matcher matcher = pattern.matcher(p);
            if (matcher.matches()) {
                urlSet.add(classLoader.getResource(p.replace('.', '/')));
            }
        }

        return urlSet;
    }

}
