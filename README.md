[![Build Status](https://travis-ci.org/pipiczistvan/puppeteer.svg)](https://travis-ci.org/pipiczistvan/puppeteer)
[![Coverage Status](https://coveralls.io/repos/github/pipiczistvan/puppeteer/badge.svg?branch=master)](https://coveralls.io/github/pipiczistvan/puppeteer?branch=master)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/3d6babdeb98e48d7a5dfe4cd5bb6924d)](https://www.codacy.com/app/istvan-pipicz/puppeteer_2?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=pipiczistvan/puppeteer&amp;utm_campaign=Badge_Grade)

# Puppeteer-Framework

Puppeteer framework provides an annotation based system, where you can specify, which mark classes should be injected to which field, or constructor.</br>
I created it for educational purposes, and the idea is based on the [Spring Framework](https://spring.io/), however you can create your own annotations, and it's more lightweight.

## Setup

Create a new instance of *Puppeteer*. The first argument is a collection of library regexps, the second is a collection of package regexps. *Puppeteer* will only load classes under these packages.

If you have a *com.test.package.Test* class, you can use:

```
Puppeteer puppeteer = new Puppeteer(
    Collections.singletonList("^.*/target/.*$"),
    Collections.singletonList("com.*.package")
);
```

If you want to use the default annotations (*Component, Wire*), do this:

```
puppeteer.useDefaultAnnotations();
```

Finally tell *Puppeteer* to process the annotations.

```
puppeteer.processAnnotations();
```

## Usage

Create an injectable class.

```
@Component
public class Test { }
```

Inject it in an other class by field, or constructor wiring.

```
@Component
public class Main {

    @Wire
    public Main(Test test) { }
}
```