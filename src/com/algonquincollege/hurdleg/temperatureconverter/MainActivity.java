package com.algonquincollege.hurdleg.temperatureconverter;

import java.util.Observable;
import java.util.Observer;

import com.tinycoolthings.double_seekbar.DoubleSeekBar;

import model.TemperatureModel;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 * View and Controller for a temperature model.
 *
 * Reference for class DoubleSeekBar: https://github.com/joaocsousa/DoubleSeekBar
 *
 * @author Gerald.Hurdle@AlgonquinCollege.com
 * @version 1
 */
public class MainActivity extends Activity implements Observer {

	public static final int ABOUT_DIALOG_ID = 10;

	private Dialog                aboutDialog;
	private TemperatureModel      model;
	private DoubleSeekBar         seekBarCelsius;
	private DoubleSeekBar         seekBarFahrenheit;
	private DoubleSeekBar         seekBarKelvin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// get the user's preferred settings
		SharedPreferences settings = getSharedPreferences( getResources().getString(R.string.app_name),
				Context.MODE_PRIVATE );

		model = new TemperatureModel( settings.getFloat("celsius", 0.0F) );
		model.addObserver( this );

		aboutDialog = onCreateDialog( ABOUT_DIALOG_ID );

		// reference the Celsius seekbar
		seekBarCelsius = (DoubleSeekBar) findViewById( R.id.seekBarCelsius );
		seekBarCelsius.setHasMax( false );
		seekBarCelsius.setMaxValue( TemperatureModel.MAX_CELSIUS );
		seekBarCelsius.setMinValue( TemperatureModel.MIN_CELSIUS );
		seekBarCelsius.setMinTitle( getResources().getString(R.string.celsius) );
		seekBarCelsius.setUnits( getResources().getString(R.string.units) );

		// reference the Fahrenheit seekbar
		seekBarFahrenheit = (DoubleSeekBar) findViewById( R.id.seekBarFahrenheit );
		seekBarFahrenheit.setHasMax( false );
		seekBarFahrenheit.setMaxValue( TemperatureModel.MAX_FAHRENHEIT );
		seekBarFahrenheit.setMinValue( TemperatureModel.MIN_FAHRENHEIT );
		seekBarFahrenheit.setMinTitle( getResources().getString(R.string.fahrenheit) );
		seekBarFahrenheit.setUnits( getResources().getString(R.string.units) );

		// reference the Kelvin seekbar
		seekBarKelvin = (DoubleSeekBar) findViewById( R.id.seekBarKelvin );
		seekBarKelvin.setHasMax( false );
		seekBarKelvin.setMaxValue( TemperatureModel.MAX_KELVIN);
		seekBarKelvin.setMinValue( TemperatureModel.MIN_KELVIN);
		seekBarKelvin.setMinTitle( getResources().getString(R.string.kelvin) );
		seekBarKelvin.setUnits( getResources().getString(R.string.kelvin) );

		// register an anonymous inner class as the event handler
		// for when the seekbar changes
		seekBarCelsius.registerOnChangeListenerMinSB( new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) { /*NOOP*/ }

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) { /*NOOP*/ }

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				model.setCelsius( seekBarCelsius.getCurrentMinValue() + 0.0F );
			}
		} );

		seekBarFahrenheit.registerOnChangeListenerMinSB( new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) { /*NOOP*/ }

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) { /*NOOP*/ }

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				model.setFahrenheit( seekBarFahrenheit.getCurrentMinValue() + 0.0F );
			}
		} );


		seekBarKelvin.registerOnChangeListenerMinSB( new OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) { /*NOOP*/ }

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) { /*NOOP*/ }

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				model.setKelvin( seekBarKelvin.getCurrentMinValue() + 0.0F );
			}
		} );


		// synch this activity with the model
		this.updateActivity();
	}
	
	@Override
	protected Dialog onCreateDialog( int id ) {
		Builder dialogBuilder = new AlertDialog.Builder( this );

		switch ( id ) {
		
		// Create About dialog
		case ABOUT_DIALOG_ID:
			dialogBuilder.setCancelable( false );
			dialogBuilder.setTitle( R.string.action_about );
			dialogBuilder.setMessage( R.string.author );
			dialogBuilder.setPositiveButton( R.string.ok_button, new DialogInterface.OnClickListener() {
				@Override
				public void onClick( DialogInterface dialog, int button ) {
					dialog.dismiss();
				}

			});
		}

		return dialogBuilder.create();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle presses on the action bar items
	    switch (item.getItemId()) {

	        case R.id.action_about:
	            aboutDialog.show();
	            return true;

	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	// Remember the user's settings
	@Override
	protected void onStop() {
		SharedPreferences settings = getSharedPreferences( getResources().getString(R.string.app_name),
				Context.MODE_PRIVATE );
		SharedPreferences.Editor editor = settings.edit();

		editor.putFloat( "celsius",   model.getCelsius() );

		editor.commit();

		super.onStop();
	}

	/**
	 * The model has changed state!
	 * Refresh the activity.
	 */
	@Override
	public void update(Observable observable, Object data) {
		this.updateActivity();
	}
	
	/**
	 * The update phase:
	 *     GET data from the model
	 *     SET view components to new values
	 */
	public void updateActivity() {
		this.updateCelsius();
		this.updateFahrenheit();
		this.updateKelvin();
	}

	private void updateCelsius() {
		// class Math's round() method accepts a float and returns an int
		// required since DoubleSeekBar only accepts int values
		seekBarCelsius.setCurrentMinValue( Math.round(model.getCelsius()) );
	}

	private void updateFahrenheit() {
		// class Math's round() method accepts a float and returns an int
		// required since DoubleSeekBar only accepts int values
		seekBarFahrenheit.setCurrentMinValue( Math.round(model.getFahrenheit()) );
	}

	private void updateKelvin() {
		// class Math's round() method accepts a float and returns an int
		// required since DoubleSeekBar only accepts int values
		seekBarKelvin.setCurrentMinValue( Math.round(model.getKelvin()) );

	}
}
