package runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = {"src/test/features"},
                glue = {"stepdefinitions"},
                monochrome = true,
                plugin= {
                        "html:target/cucumber-html-report.html", "json:target/cucumber.json",
                        "pretty:target/cucumber-pretty.txt","usage:target/cucumber-usage.json"})
public class TestRunner {
}
