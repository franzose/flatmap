package com.janiwanow.flatmap;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    plugin = { "pretty", "com.janiwanow.flatmap.CucumberEventListener" },
    glue = { "com.janiwanow.flatmap" },
    features = { "src/test/resources/features" },
    strict = true
)
public class CucumberTestRunner {
}
