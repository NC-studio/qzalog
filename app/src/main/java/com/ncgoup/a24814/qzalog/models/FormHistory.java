package com.ncgoup.a24814.qzalog.models;


import org.json.JSONObject;

import java.util.List;

public class FormHistory {

    private String url;

    private List<Form> fields;

    private JSONObject formRegion;

    public FormHistory(){
        super();
    }

    public FormHistory(String url, List<Form> fields,JSONObject formRegion) {
        super();
        this.url = url;
        this.fields = fields;
        this.formRegion = formRegion;

    }

    public String getUrl()
    {
        return url;
    }

    public List<Form> getFields()
    {
        return fields;
    }

    public JSONObject getRegion()
    {
        return formRegion;
    }


}
