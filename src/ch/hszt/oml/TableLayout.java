//package ch.hszt.oml;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup.LayoutParams;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.TableRow;
//import android.widget.TextView;
//
//public class TableLayout extends Activity implements OnClickListener {
//    /** Called when the activity is first created. */
// 
//    //initialize a button and a counter
//    Button btn;
//    int counter = 0;
// 
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
// 
//        // setup the layout
//        setContentView(R.layout.main);
// 
//        // add a click-listener on the button
//        btn = (Button) findViewById(R.id.Button01);
//        btn.setOnClickListener(this);        
// 
//    }
// 
//    // run when the button is clicked
//    public void onClick(View view) {
// 
//        // get a reference for the TableLayout
//        TableLayout table = (TableLayout) findViewById(R.id.TableLayout01);
// 
//        // create a new TableRow
//        TableRow row = new TableRow(this);
// 
//        // count the counter up by one
//        counter++;
// 
//        // create a new TextView
//        TextView t = new TextView(this);
//        // set the text to "text xx"
//        t.setText("text " + counter);
// 
//        // create a CheckBox
//        CheckBox c = new CheckBox(this);
// 
//        // add the TextView and the CheckBox to the new TableRow
//        row.addView(t);
//        row.addView(c);
// 
//        // add the TableRow to the TableLayout
//        table.addView(row,new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
// 
//    }
//}