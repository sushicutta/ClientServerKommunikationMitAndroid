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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import ch.hszt.semesterarbeit.entity.Product;

public class Semesterarbeit extends Activity implements OnClickListener {

//	private final String TAG = "OmlTest";
	
	RestClient restClient = new RestClient("http://192.168.0.135:8080/Semesterarbeit", "products");
	
	private TableLayout table;
	private Button buttonRefresh;
	private Button buttonPost;
	private Button buttonDelete;
	private Button buttonUpdate;
	
	private EditText nameEditText;
	private EditText numberOfUnitsEditText;
	private EditText idToUpdateEditText;
	
	private Integer productDeleteId;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		
		buttonRefresh = (Button)findViewById(R.id.Button01);
		buttonRefresh.setOnClickListener(this);

		buttonPost = (Button)findViewById(R.id.Button02);
		buttonPost.setOnClickListener(this);
		
		buttonDelete = (Button)findViewById(R.id.Button03);
		buttonDelete.setOnClickListener(this);
		buttonDelete.setEnabled(false);
		
		buttonUpdate = (Button)findViewById(R.id.Button04);
		buttonUpdate.setOnClickListener(this);
		
		table = (TableLayout)findViewById(R.id.TableLayout02);
		
		nameEditText = (EditText)findViewById(R.id.EditText01);
		numberOfUnitsEditText = (EditText)findViewById(R.id.EditText02);
		idToUpdateEditText = (EditText)findViewById(R.id.EditText03);
		
		JSONObject jsonObject = restClient.getJsonObject(1);
		
		addProductToTable(ProductFactory.produceFromJson(jsonObject));
	}

	// run when the button is clicked
	public void onClick(View view) {
		if (view == buttonRefresh) {
			refreshTable();			
		} else if (view == buttonPost) {
			Product product = new Product();
			product.setName(nameEditText.getText().toString());
			product.setNumberOfUnits(Integer.valueOf(numberOfUnitsEditText.getText().toString()));
			
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
					productDeleteId = Integer.valueOf(foundInteger);
					buttonDelete.setEnabled(true);
				}
			} else {
				// TODO something went wrong .. throw new Exception
			}
			refreshTable();
		} else if (view == buttonDelete) {
			if (productDeleteId != null) {
				restClient.deleteJSONObject(productDeleteId);
				buttonDelete.setEnabled(false);
			}
			refreshTable();
		} else if (view == buttonUpdate) {
			Integer idToUpdate = Integer.valueOf(idToUpdateEditText.getText().toString());
			JSONObject jsonPostObject = restClient.getJsonObject(idToUpdate);
			
			// Transfer into JAVA
			Product productToUpdate = ProductFactory.produceFromJson(jsonPostObject);
			productToUpdate.setName("Updated: " + productToUpdate.getName());
			productToUpdate.setNumberOfUnits(productToUpdate.getNumberOfUnits() + 1);
			
			jsonPostObject = JSONObjectFactory.produceFromProduct(productToUpdate);
			
			if (jsonPostObject != null) {				
				restClient.updateJSONObject(idToUpdate, jsonPostObject);			
			}
			
			refreshTable();
		} else {
			throw new IllegalStateException("Hierhin dürfte ich nicht kommen. Es wird eine Button Action nicht implementiert. [" + view +"]");
		}
	}
	
	private void refreshTable() {
		table.removeAllViews();
		JSONArray jsonArray = restClient.getJsonArray(Product.OBJECT_NAME);
		List<Product> products = ProductFactory.produceFromJson(jsonArray);
		for (Product product : products) {
			addProductToTable(product);
		}		
	}
	
	private void addProductToTable(Product product) {
		
		TextView textView = new TextView(this);
		textView.setText(product.toString());

		TableRow row = new TableRow(this);
		row.addView(textView);
		
		// add the TableRow to the TableLayout
		table.addView(row, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
	}

}
