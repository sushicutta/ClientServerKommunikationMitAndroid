package ch.hszt.semesterarbeit;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import ch.hszt.semesterarbeit.exception.ProductNotFoundException;
import ch.hszt.semesterarbeit.hessian.HessianClient;

public class Semesterarbeit extends Activity implements OnClickListener {

	private static String APPLICATION_SERVLET = "http://192.168.0.207:8080/Semesterarbeit";
//	private static String APPLICATION_SERVLET = "http://sushicutta.no-ip.org:8080/Semesterarbeit";
	
	private static String TEXT_ADD_MESSAGE = "Add text here";
	private static String NUMBER_ADD_MESSAGE = "Add number here";
	private static String EMPTY_MESSAGE = "";
	
	RestClient restClient;
	
	HessianClient hessianClient;
	
	private View.OnFocusChangeListener textFocusChangeListener;
	private View.OnFocusChangeListener numberFocusChangeListener;
	private TextWatcher restNumberTextWatcher;
	private TextWatcher hessianNumberTextWatcher;
	
	private TableLayout restTable;
	private Button restButtonRefresh;
	private Button restButtonPost;
	private Button restButtonDelete;
	private Button restButtonUpdate;
	
	private EditText restNameEditText;
	private EditText restNumberOfUnitsEditText;
	private EditText restIdToUpdateEditText;
	private EditText restIdToDeleteEditText;
	
	private TextView restStatus;
	
	private TableLayout hessianTable;
	private Button hessianButtonRefresh;
	private Button hessianButtonInsert;
	private Button hessianButtonDelete;
	private Button hessianButtonUpdate;
	
	private EditText hessianNameEditText;
	private EditText hessianNumberOfUnitsEditText;
	private EditText hessianIdToUpdateEditText;
	private EditText hessianIdToDeleteEditText;
	
	private TextView hessianStatus;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		
		//Setup the Tab-View for Rest & Hessian
		
		TabHost tabHost=(TabHost)findViewById(R.id.tabHost);
		tabHost.setup();

		TabSpec spec1=tabHost.newTabSpec("Rest");
		spec1.setContent(R.id.Rest);
		spec1.setIndicator("Rest");

		TabSpec spec2=tabHost.newTabSpec("Hessian");
		spec2.setIndicator("Hessian");
		spec2.setContent(R.id.Hessian);

		tabHost.addTab(spec1);
		tabHost.addTab(spec2);

		setupListener();
		
		setupRest();
		
		setupHessian();
		
	}
	
	private void setupListener() {
		
		textFocusChangeListener = new View.OnFocusChangeListener() {
		    @Override
		    public void onFocusChange(View v, boolean hasFocus) {
		        if (hasFocus && ((EditText)v).getText().toString().equals(TEXT_ADD_MESSAGE)) {
		        	((EditText)v).setText(EMPTY_MESSAGE);
		        } else if (!hasFocus && ((EditText)v).getText().toString().equals(EMPTY_MESSAGE)) {
		        	((EditText)v).setText(TEXT_ADD_MESSAGE);
		        }
		    }
		};
		
		numberFocusChangeListener = new View.OnFocusChangeListener() {
		    @Override
		    public void onFocusChange(View v, boolean hasFocus) {
		        if (hasFocus && ((EditText)v).getText().toString().equals(NUMBER_ADD_MESSAGE)) {
		        	((EditText)v).setText(EMPTY_MESSAGE);
		        } else if (!hasFocus && ((EditText)v).getText().toString().equals(EMPTY_MESSAGE)) {
		        	((EditText)v).setText(NUMBER_ADD_MESSAGE);
		        }
		    }
		};
		
		restNumberTextWatcher = new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				restButtonDelete.setEnabled(!(s.toString().equals(NUMBER_ADD_MESSAGE) || s.toString().equals(EMPTY_MESSAGE)));
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// do nothing
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// do nothing
			}
		};
		
		hessianNumberTextWatcher = new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				hessianButtonDelete.setEnabled(!(s.toString().equals(NUMBER_ADD_MESSAGE) || s.toString().equals(EMPTY_MESSAGE)));
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// do nothing
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// do nothing
			}
		};
		
	}
	
	private void setupRest() {
		
		restClient = new RestClient(APPLICATION_SERVLET, "products");
		
		restStatus = (TextView)findViewById(R.id.RestTextView06);
		
		restButtonRefresh = (Button)findViewById(R.id.RestButton01);
		restButtonRefresh.setOnClickListener(this);

		restButtonPost = (Button)findViewById(R.id.RestButton02);
		restButtonPost.setOnClickListener(this);
		
		restButtonDelete = (Button)findViewById(R.id.RestButton03);
		restButtonDelete.setOnClickListener(this);
		restButtonDelete.setEnabled(false);
		
		restButtonUpdate = (Button)findViewById(R.id.RestButton04);
		restButtonUpdate.setOnClickListener(this);
		
		restTable = (TableLayout)findViewById(R.id.RestTableLayout02);
		
		restNameEditText = (EditText)findViewById(R.id.RestEditText01);
		restNameEditText.setOnFocusChangeListener(textFocusChangeListener);
		restNumberOfUnitsEditText = (EditText)findViewById(R.id.RestEditText02);
		restNumberOfUnitsEditText.setOnFocusChangeListener(numberFocusChangeListener);
		restIdToUpdateEditText = (EditText)findViewById(R.id.RestEditText03);
		restIdToUpdateEditText.setOnFocusChangeListener(numberFocusChangeListener);
		restIdToDeleteEditText = (EditText)findViewById(R.id.RestEditText04);
		restIdToDeleteEditText.setOnFocusChangeListener(numberFocusChangeListener);
		restIdToDeleteEditText.addTextChangedListener(restNumberTextWatcher);
		
		try {
			JSONObject jsonObject = restClient.getJsonObject(1);
			if (jsonObject != null) {
				addProductToRestTable(ProductFactory.produceFromJson(jsonObject));
				setRestStatusText(restClient.getStatusCode() + " >>> Produkt " + 1 + " gelesen.", false);
			}
		} catch (ProductNotFoundException e) {
			setRestStatusText(restClient.getStatusCode() + " >>> Produkt " + 1 + " nicht gefunden.", true);
		} catch (Exception e) {
			setRestStatusText(restClient.getStatusCode() + " >>> Fehler aufgetreten.", true);
		}
		
	}

	private void setupHessian() {
		
		try {
			hessianClient = new HessianClient(APPLICATION_SERVLET);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		hessianStatus = (TextView)findViewById(R.id.HessianTextView06);
		
		hessianButtonRefresh = (Button)findViewById(R.id.HessianButton01);
		hessianButtonRefresh.setOnClickListener(this);

		hessianButtonInsert = (Button)findViewById(R.id.HessianButton02);
		hessianButtonInsert.setOnClickListener(this);
		
		hessianButtonDelete = (Button)findViewById(R.id.HessianButton03);
		hessianButtonDelete.setOnClickListener(this);
		hessianButtonDelete.setEnabled(false);
		
		hessianButtonUpdate = (Button)findViewById(R.id.HessianButton04);
		hessianButtonUpdate.setOnClickListener(this);
		
		hessianTable = (TableLayout)findViewById(R.id.HessianTableLayout02);
		
		hessianNameEditText = (EditText)findViewById(R.id.HessianEditText01);
		hessianNameEditText.setOnFocusChangeListener(textFocusChangeListener);
		hessianNumberOfUnitsEditText = (EditText)findViewById(R.id.HessianEditText02);
		hessianNumberOfUnitsEditText.setOnFocusChangeListener(numberFocusChangeListener);
		hessianIdToUpdateEditText = (EditText)findViewById(R.id.HessianEditText03);
		hessianIdToUpdateEditText.setOnFocusChangeListener(numberFocusChangeListener);
		
		hessianIdToDeleteEditText = (EditText)findViewById(R.id.HessianEditText04);
		hessianIdToDeleteEditText.setOnFocusChangeListener(numberFocusChangeListener);
		hessianIdToDeleteEditText.addTextChangedListener(hessianNumberTextWatcher);
		
		Product product = null;
		try {
			product = hessianClient.get(1l);
		} catch (Exception e) {
			setHessianStatusText("Select " + 1l + " fehlgeschlagen.", true);
		}
		
		if (product != null) {
			addProductToHessianTable(product);
			setHessianStatusText("Produkt geladen.", false);
		}
		
	}
	
	// run when the button is clicked
	public void onClick(View view) {
		if (view == restButtonRefresh) {
			onRestButtonRefreshClicked();
		} else if (view == restButtonPost) {
			onRestButtonPostClicked();
		} else if (view == restButtonDelete) {
			onRestButtonDeleteClicked();
		} else if (view == restButtonUpdate) {
			onRestButtonUpdateClicked();
		} else if (view == hessianButtonRefresh) {
			onHessianButtonRefreshClicked();
		} else if (view == hessianButtonInsert) {
			onHessianButtonInsertClicked();
		} else if (view == hessianButtonDelete) {
			onHessianButtonDeleteClicked();
		} else if (view == hessianButtonUpdate) {
			onHessianButtonUpdateClicked();
		} else {
			throw new IllegalStateException("Es wird eine Button-Action nicht implementiert in der View: [" + view +"]");
		}
	}
	
	private void refreshRestTable() {
		
		try {
			restTable.removeAllViews();
			JSONArray jsonArray = restClient.getJsonArray(Product.OBJECT_NAME);
			List<Product> products = ProductFactory.produceFromJson(jsonArray);
			for (Product product : products) {
				addProductToRestTable(product);
			}
		} catch (ProductNotFoundException e) {
			setRestStatusText(restClient.getStatusCode() + " >>> Produkte nicht gefunden.", true);
		} catch (Exception e) {
			setRestStatusText(restClient.getStatusCode() + " >>> Fehler aufgetreten.", true);
		}
		
	}
	
	private void addProductToRestTable(Product product) {
		
		TextView textView = new TextView(this);
		textView.setText(product.toString());

		TableRow row = new TableRow(this);
		row.addView(textView);
		
		// add the TableRow to the TableLayout
		restTable.addView(row, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
	}
	
	private void setRestStatusText(String statusText, boolean error) {
		restStatus.setText(statusText);
		if (error) {
			restStatus.setTextColor(Color.RED);
		} else {
			restStatus.setTextColor(Color.GREEN);
		}
	}
	
	private void refreshHessianTable() {
		
		hessianTable.removeAllViews();
		List<Product> products = hessianClient.allProducts();
		for (Product product : products) {
			addProductToHessianTable(product);
		}
		
	}
	
	private void addProductToHessianTable(Product product) {
		
		TextView textView = new TextView(this);
		textView.setText(product.toString());

		TableRow row = new TableRow(this);
		row.addView(textView);
		
		// add the TableRow to the TableLayout
		hessianTable.addView(row, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
	}
	
	private void setHessianStatusText(String statusText, boolean error) {
		hessianStatus.setText(statusText);
		if (error) {
			hessianStatus.setTextColor(Color.RED);
		} else {
			hessianStatus.setTextColor(Color.GREEN);
		}
	}

	
	private void onRestButtonRefreshClicked() {
		refreshRestTable();
		setRestStatusText(restClient.getStatusCode() + " >>> Alle Produkt gelesen.", false);
	}
	
	private void onRestButtonPostClicked() {
		Product product = new Product();
		product.setName(restNameEditText.getText().toString());
		product.setNumberOfUnits(Integer.valueOf(restNumberOfUnitsEditText.getText().toString()));
		
		JSONObject jsonObject = JSONObjectFactory.produceFromProduct(product);

		try {
				
			String location = restClient.insertJSONObject(jsonObject);
			if (location != null) {
				URI uri = new URI (location);
				Pattern p = Pattern.compile("\\d+");
				Matcher m = p.matcher(uri.getPath());
				if (m.find()) {
					String foundInteger = m.group();
					Integer addedProductId = Integer.valueOf(foundInteger);
					setRestStatusText(restClient.getStatusCode() + " >>> Produkt " + addedProductId + " erfasst.", false);
				} else {
					throw new Exception("Das Produkt wurde erfasst, aber im Header wurde keine Location angegeben.");
				}
			}

		} catch (Exception e) {
			setRestStatusText(restClient.getStatusCode() + " >>> Fehler aufgetreten.", true);
			return;
		}

		refreshRestTable();
	}
	
	private void onRestButtonDeleteClicked() {
		Integer idToDelete = Integer.valueOf(restIdToDeleteEditText.getText().toString());
		try {
			restClient.deleteJSONObject(idToDelete);
			setRestStatusText(restClient.getStatusCode() + " >>> Produkt " + idToDelete + " gelöscht.", false);
		} catch (ProductNotFoundException e) {
			setRestStatusText(restClient.getStatusCode() + " >>> Produkt " + idToDelete + " nicht gefunden.", true);
			return;
		} catch (Exception e) {
			setRestStatusText(restClient.getStatusCode() + " >>> Fehler aufgetreten.", true);
			return;
		}
		refreshRestTable();		
	}
	
	private void onRestButtonUpdateClicked() {
		Integer idToUpdate = Integer.valueOf(restIdToUpdateEditText.getText().toString());
		
		JSONObject jsonPostObject = null;
		
		try {
			jsonPostObject = restClient.getJsonObject(idToUpdate);
		} catch (ProductNotFoundException e) {
			setRestStatusText(restClient.getStatusCode() + " >>> Produkt " + idToUpdate + " nicht gefunden.", true);
			return;
		} catch (Exception e) {
			setRestStatusText(restClient.getStatusCode() + " >>> Fehler aufgetreten.", true);
			return;
		}
		
		if (jsonPostObject != null) {
			// Transfer into JAVA
			Product productToUpdate = ProductFactory.produceFromJson(jsonPostObject);
			productToUpdate.setName("Updated: " + productToUpdate.getName());
			productToUpdate.setNumberOfUnits(productToUpdate.getNumberOfUnits() + 1);
			
			jsonPostObject = JSONObjectFactory.produceFromProduct(productToUpdate);
			
			if (jsonPostObject != null) {				
				try {
					restClient.updateJSONObject(idToUpdate, jsonPostObject);
					setRestStatusText(restClient.getStatusCode() + " >>> Produkt " + idToUpdate + " updated.", false);
				} catch (ProductNotFoundException e) {
					setRestStatusText(restClient.getStatusCode() + " >>> Produkt " + idToUpdate + " nicht gefunden.", true);
					return;
				} catch (Exception e) {
					setRestStatusText(restClient.getStatusCode() + " >>> Fehler aufgetreten.", true);
					return;
				}			
			}
		} else {
			setRestStatusText(restClient.getStatusCode() + " >>> Fehler aufgetreten.", true);
			return;
		}
		
		refreshRestTable();		
	}

	private void onHessianButtonRefreshClicked () {
		refreshHessianTable();
		setHessianStatusText("Alle Produkte geladen.", false);
	}
	
	private void onHessianButtonInsertClicked () {
		try {
			Product product = new Product();
			product.setName(hessianNameEditText.getText().toString());
			product.setNumberOfUnits(Integer.valueOf(hessianNumberOfUnitsEditText.getText().toString()));
			hessianClient.register(product);
		} catch (Exception e) {
			setHessianStatusText("Save fehlgeschlagen.", true);
		}
		refreshHessianTable();
		setHessianStatusText("Produkt gespeichert.", false);
	}
	
	private void onHessianButtonDeleteClicked () {
		Integer idToDelete = Integer.valueOf(hessianIdToDeleteEditText.getText().toString());
		try {
			hessianClient.delete((long)idToDelete);
		} catch (Exception e) {
			setHessianStatusText("Delete fehlgeschlagen.", true);
		}
		refreshHessianTable();	
		setHessianStatusText("Produkt " + idToDelete + " gelöscht.", false);
	}
	
	private void onHessianButtonUpdateClicked () {
		Integer idToUpdate = Integer.valueOf(hessianIdToUpdateEditText.getText().toString());
		
		Product productToUpdate;
		try {
			productToUpdate = hessianClient.get((long)idToUpdate);
			productToUpdate.setName("Updated: " + productToUpdate.getName());
			productToUpdate.setNumberOfUnits(productToUpdate.getNumberOfUnits() + 1);
			hessianClient.update((long)idToUpdate, productToUpdate);			
		} catch (Exception e) {
			setHessianStatusText("Update fehlgeschlagen.", true);
		}
		refreshHessianTable();
		setHessianStatusText("Produkt " + idToUpdate + " aktualisiert.", false);
	}
	
}
