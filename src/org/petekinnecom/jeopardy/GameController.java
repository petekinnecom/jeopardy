package org.petekinnecom.jeopardy;

import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class GameController extends Activity implements OnClickListener, OnInitListener
{

	private static final String TAG = "PK";
	TextView cView, vView, qView, leftButtonView, rightButtonView;
	private TextToSpeech tts;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.clue);

		tts = new TextToSpeech(this, this);
		
		vView = (TextView) findViewById(R.id.valueText);
		cView = (TextView) findViewById(R.id.categoryText);
		qView = (TextView) findViewById(R.id.clueText);
		leftButtonView = (TextView) findViewById(R.id.wrongButton);
		rightButtonView = (TextView) findViewById(R.id.rightButton);
		
		// Set up Listeners
		findViewById(R.id.wrongButton).setOnClickListener(this);
		findViewById(R.id.rightButton).setOnClickListener(this);
		findViewById(R.id.backButton).setOnClickListener(this);
		findViewById(R.id.answerButton).setOnClickListener(this);
		qView.setOnClickListener(this);
		showClue();
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.wrongButton:
		{
			C.clueIndex++;
			findViewById(R.id.answerButton).setVisibility(View.VISIBLE);
			findViewById(R.id.backButton).setVisibility(View.VISIBLE);
			showClue();
			break;
		}
		case R.id.rightButton:
		{
			C.clueIndex++;
			findViewById(R.id.answerButton).setVisibility(View.VISIBLE);
			findViewById(R.id.backButton).setVisibility(View.VISIBLE);
			showClue();
			break;
		}
		case R.id.backButton:
		{
			C.clueIndex--;
			if (C.clueIndex < 0)
				C.clueIndex = 0;
			showClue();
			break;
		}
		case R.id.answerButton:
		{
			showAnswer();
			findViewById(R.id.answerButton).setVisibility(View.GONE);
			findViewById(R.id.backButton).setVisibility(View.GONE);
			
			break;
		}
		case R.id.clueText:
		{
			showClue();
			break;
		}
		}
		
	}

	private void showAnswer()
	{
		qView.setText(C.clues.get(C.clueIndex).a);
		tts.speak(C.clues.get(C.clueIndex).a, TextToSpeech.QUEUE_FLUSH, null);
	}
	
	private void showClue()
	{
		if (C.clueIndex >= C.clues.size())
		{
			cView.setText("NO MORE");
			vView.setText("0");
			qView.setText("You have reached the end.");
		} else
		{
			Clue c = C.clues.get(C.clueIndex);
			cView.setText(c.c);
			vView.setText("" + c.v);
			qView.setText(c.q);
			tts.speak(C.clues.get(C.clueIndex).q, TextToSpeech.QUEUE_FLUSH, null);
		}
	}

	@Override
	public void onInit(int status)
	{
        if (status == TextToSpeech.SUCCESS) {
            Log.d(TAG, "tts inited.");
      }
      else if (status == TextToSpeech.ERROR) {
    	  Log.d(TAG, "tts init failed");
      }
		
	}
}
