package com.android.porta.pk.toolbox;

import com.android.volley.VolleyError;

public interface RequestStatusListener {

	public void onSuccess();

	public void onError(VolleyError error);

}
