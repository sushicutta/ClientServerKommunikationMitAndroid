package ch.hszt.semesterarbeit;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
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
import ch.hszt.semesterarbeit.entity.Product;

public class Semesterarbeit extends Activity implements OnClickListener {

//	private final String TAG = "OmlTest";
	
	private static String TEXT_ADD_MESSAGE = "Add text here";
	private static String NUMBER_ADD_MESSAGE = "Add number here";
	private static String EMPTY_MESSAGE = "";
	
	RestClient restClient = new RestClient("http://192.168.0.135:8080/Semesterarbeit", "products");
	
	private View.OnFocusChangeListener textFocusChangeListener;
	private View.OnFocusChangeListener numberFocusChangeListener;
	private View.OnKeyListener numberKeyListener;
	
	private TableLayout restTable;
	private Button restButtonRefresh;
	private Button restButtonPost;
	private Button restButtonDelete;
	private Button restButtonUpdate;
	
	private EditText restNameEditText;
	private EditText restNumberOfUnitsEditText;
	private EditText restIdToUpdateEditText;
	private EditText restIdToDeleteEditText;
	
	private TableLayout hessianTable;
	private Button hessianButtonRefresh;
	private Button hessianButtonInsert;
	private Button hessianButtonDelete;
	private Button hessianButtonUpdate;
	
	private EditText hessianNameEditText;
	private EditText hessianNumberOfUnitsEditText;
	private EditText hessianIdToUpdateEditText;
	
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

		numberKeyListener = new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
	    		restButtonDelete.setEnabled(!(((EditText)v).getText().toString().equals(NUMBER_ADD_MESSAGE) || ((EditText)v).getText().toString().equals(EMPTY_MESSAGE)));
				return false;
			}
		};
	}
	
	private void setupRest() {
		
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
		
		restIdToDeleteEditText.addTextChangedListener(new TextWatcher() {
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
		});
		
		JSONObject jsonObject = restClient.getJsonObject(1);
		
		addProductToRestTable(ProductFactory.produceFromJson(jsonObject));
		
	}

	private void setupHessian() {
		
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
		
//		JSONObject jsonObject = hessianClient.getJsonObject(1);
//		
//		addProductToHessianTable(ProductFactory.produceFromJson(jsonObject));
		
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
		
		restTable.removeAllViews();
		JSONArray jsonArray = restClient.getJsonArray(Product.OBJECT_NAME);
		List<Product> products = ProductFactory.produceFromJson(jsonArray);
		for (Product product : products) {
			addProductToRestTable(product);
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
	
	
	private void addProductToHessianTable(Product product) {
		
		TextView textView = new TextView(this);
		textView.setText(product.toString());

		TableRow row = new TableRow(this);
		row.addView(textView);
		
		// add the TableRow to the TableLayout
		hessianTable.addView(row, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
	}
	
	
	private void onRestButtonRefreshClicked() {
		refreshRestTable();			
	}
	
	private void onRestButtonPostClicked() {
		Product product = new Product();
		product.setName(restNameEditText.getText().toString());
		product.setNumberOfUnits(Integer.valueOf(restNumberOfUnitsEditText.getText().toString()));
		
		JSONObject jsonObject = JSONObjectFactory.produceFromProduct(product);
		
		String location = restClient.insertJSONObject(jsonObject);
		URI uri = null;
		try {
			uri = new URI (location);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		if (uri != null) {
			Pattern p = Pattern.compile("\\d+");
			Matcher m = p.matcher(uri.getPath());
			if (m.find()) {
				String foundInteger = m.group();
				Integer addedProductId = Integer.valueOf(foundInteger);
			}
		} else {
			// TODO something went wrong .. throw new Exception
		}
		refreshRestTable();
	}
	
	private void onRestButtonDeleteClicked() {
		Integer idToDelete = Integer.valueOf(restIdToDeleteEditText.getText().toString());
		restClient.deleteJSONObject(idToDelete);
		refreshRestTable();		
	}
	
	private void onRestButtonUpdateClicked() {
		Integer idToUpdate = Integer.valueOf(restIdToUpdateEditText.getText().toString());
		JSONObject jsonPostObject = restClient.getJsonObject(idToUpdate);
		
		// Transfer into JAVA
		Product productToUpdate = ProductFactory.produceFromJson(jsonPostObject);
		productToUpdate.setName("Updated: " + productToUpdate.getName());
		productToUpdate.setNumberOfUnits(productToUpdate.getNumberOfUnits() + 1);
		
		jsonPostObject = JSONObjectFactory.produceFromProduct(productToUpdate);
		
		if (jsonPostObject != null) {				
			restClient.updateJSONObject(idToUpdate, jsonPostObject);			
		}
		
		refreshRestTable();		
	}

	private void onHessianButtonRefreshClicked () {
		
	}
	
	private void onHessianButtonInsertClicked () {
		
	}
	
	private void onHessianButtonDeleteClicked () {
		
	}
	
	private void onHessianButtonUpdateClicked () {
		
	}
	
}
