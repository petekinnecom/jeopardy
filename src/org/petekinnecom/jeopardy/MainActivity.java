package org.petekinnecom.jeopardy;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity
{
	private static final String TAG = "PK";

	ArrayList<Clue> clues;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);	
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		
		alert.setTitle("Select game");
		alert.setMessage("Number: ");

		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		input.setText("");
		alert.setView(input);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton)
			{
				String num = input.getText().toString();
					Log.d(TAG, "getting level: "+num);
					setGame(num);
					ClueGetter clueGetter = new ClueGetter();
					clueGetter.execute();
			}
		});
		alert.show();
	}
	String url;
	public void setGame(String s)
	{
		url = "http://www.j-archive.com/showgame.php?game_id="+s;
	}
	public void startGame()
	{
		Collections.sort(clues);
		C.clues = clues;
		// Start us at the first clue
		C.clueIndex = 0;

		TextView v = (TextView) findViewById(R.id.textview);
		Intent i = new Intent(this, GameController.class);
		startActivity(i);
	}

	private class ClueGetter extends AsyncTask<Void, Void, Void>
	{
		protected Void doInBackground(Void... v)
		{

			Log.d(TAG, "getting clues.");
			getClues();

			return null;
		}

		protected void onProgressUpdate()
		{
		}

		protected void onPostExecute(Void v)
		{
			Log.d(TAG, "starting game.");
			startGame();
		}

	}

	private void getClues()
	{

		String html = "";
		try
		{

			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(url);
			HttpResponse response = client.execute(request);

			InputStream in = response.getEntity().getContent();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			StringBuilder str = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null)
			{
				str.append(line);
			}
			in.close();
			html = str.toString();

			Log.d("PK", "html: " + html);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		clues = new ArrayList<Clue>();
		ArrayList<String> qs = new ArrayList<String>();
		ArrayList<String> as = new ArrayList<String>();
		ArrayList<String> cs = new ArrayList<String>();
		ArrayList<Integer> rowy = new ArrayList<Integer>();
		ArrayList<Integer> colx = new ArrayList<Integer>();

		Log.d(TAG, "trying to match.");

		// Find Categories

		Pattern p = Pattern.compile("<td class=\"category_name\">(.*?)</td>");
		Matcher m = p.matcher(html);

		while (m.find())
		{
			cs.add(m.group(1));
		}

		// Find clue
		p = Pattern.compile("clue_text\">(.*?)</td>");
		m = p.matcher(html);

		while (m.find())
		{

			qs.add(m.group(1));
		}

		// Find answer
		p = Pattern
				.compile("correct_response&quot;&gt;(.*?)&lt;/em&gt;");
		// p = Pattern.compile("correct_response&quot;&gt;(.*?)&lt;/em&gt;");
		m = p.matcher(html);
		while (m.find())
		{
			//as.add(m.group(1));
			as.add(android.text.Html.fromHtml(m.group(1)).toString());
		}

		// Find row
		p = Pattern.compile("onmouseover=\"toggle\\('clue_D?J_(.?)_");
		// p = Pattern.compile("correct_response&quot;&gt;(.*?)&lt;/em&gt;");
		m = p.matcher(html);
		while (m.find())
		{
			colx.add(Integer.parseInt("0" + m.group(1)));
		}

		// Find column
		p = Pattern.compile("onmouseover=\"toggle\\('clue_D?J_._(.?)");
		// p = Pattern.compile("correct_response&quot;&gt;(.*?)&lt;/em&gt;");
		m = p.matcher(html);
		while (m.find())
		{
			rowy.add(Integer.parseInt("0" + m.group(1)));
		}

		int i = 0;
		int cat = 0;
		int dj = 0;
		boolean djFlag = false;
		while (i < qs.size() && i < as.size() && i < colx.size()
				&& i < rowy.size())
		{

			if (djFlag == true && rowy.get(i) == 1)
				dj = 6;
			 Log.d(TAG, "\n--------");
			 Log.d(TAG, "cat: "+cs.get(colx.get(i)+dj - 1));
			 Log.d(TAG, "("+(colx.get(i)+dj)+", "+rowy.get(i)+")");
			 Log.d(TAG, "" + qs.get(i));
			 Log.d(TAG, "" + as.get(i));
			clues.add(new Clue((colx.get(i) + dj), rowy.get(i), qs.get(i), as
					.get(i), cs.get(colx.get(i) + dj - 1)));
			if (rowy.get(i) > 1)
				djFlag = true;

			i++;

		}

		Log.d(TAG,
				"qs: " + qs.size() + " as: " + as.size() + " col: "
						+ rowy.size() + " row: " + colx.size());

	}
}