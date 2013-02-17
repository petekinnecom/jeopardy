package org.petekinnecom.jeopardy;

import android.util.Log;

public class Clue implements Comparable
{
	public String q, a, c;
	public int x, y, v;
	public boolean gotCorrect = false;
	
	public Clue(int x, int y, String q, String a, String c)
	{
		this.x = x;
		this.y = y;
		this.q = android.text.Html.fromHtml(q).toString();
		this.a = android.text.Html.fromHtml(a).toString();
		this.c = android.text.Html.fromHtml(c).toString();
		this.v = 100*y;
		if(x>6)
			this.v = 200*y;
	}

	@Override
	public int compareTo(Object o)
	{
		Clue c = (Clue) o;
		if(c.x>this.x)
			return -1;
		if(c.x<this.x)
			return 1;
		if(c.y>this.y)
			return -1;
		if(c.y<this.y)
			return 1;
		Log.d("PK", "ERROR IN COMPARE.");
		return 0;
	}
	
	public String toString()
	{
		return "------\n" +
				""+c+"\n"+
				"("+x+", "+y+")\n"+
				q+"\n"+
				a;
		
	}
}
