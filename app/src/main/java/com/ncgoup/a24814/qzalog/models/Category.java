package com.ncgoup.a24814.qzalog.models;


public class Category {
    private String name;
    private String objAmount;
    private Integer objectId;
    private Integer type;

    public Category(){
        super();
    }




    public Category(String name, Integer objAmount, Integer objectId) {
        super();
        this.name = name;
        if(objAmount != null)
            this.objAmount = String.valueOf(objAmount);
        else
            this.objAmount = "";

        this.objectId = objectId;
        this.type = 0;
    }

    public Category(String name) {
        super();
        this.name = name;
        this.objAmount = "";
        this.objectId = 1;
        this.type = 1;
    }


    public String getName()
    {
        return name;
    }

    public String getObjAmount()
    {
        return objAmount;
    }
    public void setObjAmount(Integer objAmount)
    {
        this.objAmount = String.valueOf(objAmount);
    }

    public Integer getObjectId()
    {
        return objectId;
    }

    public Integer getType()
    {
        return type;
    }



}
