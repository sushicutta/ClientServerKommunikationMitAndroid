package ch.hszt.oml;

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

public class OmlTest extends Activity implements OnClickListener {

//	private final String TAG = "OmlTest";
	
	private TableLayout table;
	private Button buttonRefresh;
	private Button buttonPost;
	private Button buttonDelete;

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
		
		table = (TableLayout)findViewById(R.id.TableLayout02);
		
		JSONObject jsonObject = RestGetter.getJsonObject("http://oml.orwell.ch", "de", "movies", 1);
		Movie movie = MovieFactory.produceFromJson(jsonObject);
		
		addMovieToTable(movie);
	}

	// run when the button is clicked
	public void onClick(View view) {
		if (view == buttonRefresh) {
			table.removeAllViews();
			JSONArray jsonArray = RestGetter.getJsonArray("http://oml.orwell.ch", "de", "movies");
			List<Movie> movies = MovieFactory.produceFromJson(jsonArray);
			for (Movie movie : movies) {
				addMovieToTable(movie);
			}			
		} else if (view == buttonPost) {
			MoviePost moviePost = new MoviePost();
			moviePost.setTitle("Test POST from Android");
			moviePost.setDescription("Beschreibung");
			
			JSONObject jsonObject = JSONObjectFactory.produceFromMovie(moviePost);
			
			RestGetter.postJSONObject("http://oml.orwell.ch/de/movies.json", jsonObject);			
		} else if (view == buttonDelete) {
			// nothing yet
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
