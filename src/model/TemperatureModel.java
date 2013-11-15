package model;

import java.util.Observable;

/**
 * Model the following temperature scales:
 *
 *    Celsius
 *    Fahrenheit
 *    Kelvin
 *
 * Common references:
 *    Absolute Zero, -459.67 F
 *    Freezing point of water, 32 F
 *    Warm summer's day in a temperate climate, 72 F
 *    Normal human body temperature, 98.6 F
 *    Boiling point of water at 1 atmosphere, 212 F
 *
 * Reference: http://en.wikipedia.org/wiki/Temperature_conversion
 *
 * @author Gerald.Hurdle@AlgonquinCollege.com
 * @version 1
 */
public class TemperatureModel extends Observable {
	
	public static final int MAX_CELSIUS    = 5500;
	public static final int MIN_CELSIUS    = -273;
	public static final int MAX_FAHRENHEIT = 9900;
	public static final int MIN_FAHRENHEIT = -459;
	public static final int MAX_KELVIN     = 5800;
	public static final int MIN_KELVIN     = 0;

	private Float celsius;
	private Float fahrenheit;
	private Float kelvin;

	public TemperatureModel() {
		this( 0.0F );
	}

	public TemperatureModel( Float celsius ) {
		super();

		this.setCelsius( celsius );
	}

	private void convertCelsiusToFahrenheit() {
		fahrenheit = (celsius * 9/5) + 32.0F;
	}

	private void convertCelsiusToKelvin() {
		kelvin = (float) (celsius + 273.15F);
	}

	private void convertFahrenheitToCelsius() {
		celsius = (float) ((fahrenheit - 32.0F) * 5/9);
	}

	private void convertFahrenheitToKelvin() {
		kelvin = (float) ((fahrenheit + 459.67F) * 5/9);
	}

	private void convertKelvinToCelsius() {
		celsius = (float) (kelvin - 273.15F);
	}

	private void convertKelvinToFahrenheit() {
		fahrenheit = (float) ((kelvin * 9/5) - 459.67F);
	}

	public Float getCelsius() {
		return celsius;
	}

	public Float getFahrenheit() {
		return fahrenheit;
	}

	public Float getKelvin() {
		return kelvin;
	}

	public void setCelsius(Float celsius) {
		this.celsius = celsius;
		
		convertCelsiusToFahrenheit();
		convertCelsiusToKelvin();

		updateObservers();
	}

	public void setFahrenheit(Float fahrenheit) {
		this.fahrenheit = fahrenheit;
		
		convertFahrenheitToCelsius();
		convertFahrenheitToKelvin();

		updateObservers();
	}

	public void setKelvin(Float kelvin) {
		this.kelvin = kelvin;

		convertKelvinToCelsius();
		convertKelvinToFahrenheit();

		updateObservers();
	}

	/**
	 * The model has changed state!
	 * Notify all registered observers.
	 */
	private void updateObservers() {
		this.setChanged();
		this.notifyObservers();
	}
}
