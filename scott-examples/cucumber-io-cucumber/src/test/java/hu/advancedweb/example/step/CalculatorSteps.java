package hu.advancedweb.example.step;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import hu.advancedweb.example.Calculator;

public class CalculatorSteps {

	public static Calculator calculator;

	@Given("^a Calculator")
	public void a_calculator() {
		calculator = new Calculator();
	}

	@When("^I add (\\d+) and (\\d+)$")
    public void i_add_two_numbers(String arg1, String arg2) {
		calculator.push(arg1);
        calculator.push(arg2);
		calculator.push("+");
    }

	@Then("^the result is (\\d+)$")
	public void the_result_is(String expected) {
		String result = calculator.evaluate();
		assertThat(result, equalTo(expected));
	}

}
