package ch.hszt.semesterarbeit;

import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class Semesterarbeit extends Activity implements OnClickListener {

//	private final String TAG = "OmlTest";
	
	RestClient restClient = new RestClient("http://oml.orwell.ch", "de", "movies");
	
	private TableLayout table;
	private Button buttonRefresh;
	private Button buttonPost;
	private Button buttonDelete;
	private Button buttonUpdate;
	
	private Movie movieDelete;
	
	private Movie movieUpdate;

//	/** Called when the activity is first created. */
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
		
		buttonUpdate = (Button)findViewById(R.id.Button04);
		buttonUpdate.setOnClickListener(this);
		
		table = (TableLayout)findViewById(R.id.TableLayout02);
		
		JSONObject jsonObject = restClient.getJsonObject(1);
		movieUpdate = MovieFactory.produceFromJson(jsonObject);
		
		addMovieToTable(movieUpdate);
	}

	// run when the button is clicked
	public void onClick(View view) {
		if (view == buttonRefresh) {
			table.removeAllViews();
			JSONArray jsonArray = restClient.getJsonArray();
			List<Movie> movies = MovieFactory.produceFromJson(jsonArray);
			for (Movie movie : movies) {
				addMovieToTable(movie);
			}			
			movieDelete = movies.get(movies.size()-1);
		} else if (view == buttonPost) {
			MoviePost moviePost = new MoviePost();
			moviePost.setTitle("Test POST from Android");
			moviePost.setDescription("Beschreibung");
			
			JSONObject jsonObject = JSONObjectFactory.produceFromMovie(moviePost);
			
			restClient.insertJSONObject(jsonObject);			
		} else if (view == buttonDelete) {
			JSONObject jsonObject = null;
			if (movieDelete != null) {
				jsonObject = JSONObjectFactory.produceFromMovie(movieDelete);
			}
			if (jsonObject != null) {				
				restClient.deleteJSONObject(movieDelete.getId());			
			}
		} else if (view == buttonUpdate) {
			JSONObject jsonObject = null;
			if (movieUpdate != null) {
				
				MoviePost moviePost = new MoviePost();
				moviePost.setTitle(String.valueOf(new Date().getTime()));
				moviePost.setDescription(movieUpdate.getDescription());

				jsonObject = JSONObjectFactory.produceFromMovie(moviePost);
			}
			if (jsonObject != null) {				
				restClient.updateJSONObject(movieUpdate.getId(), jsonObject);			
			}
		} else {
			throw new IllegalStateException("Hierhin dürfte ich nicht kommen. Es wird eine Button Action nicht implementiert. [" + view +"]");
		}
	}
	
	private void addMovieToTable(Movie movie) {
		
		TextView textView = new TextView(this);
		textView.setText(movie.toString());

		TableRow row = new TableRow(this);
		row.addView(textView);
		
		// add the TableRow to the TableLayout
		table.addView(row, new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
	}

}
