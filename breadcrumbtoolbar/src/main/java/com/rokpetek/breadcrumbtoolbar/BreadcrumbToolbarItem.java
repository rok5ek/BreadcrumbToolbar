package com.rokpetek.breadcrumbtoolbar;

import java.io.Serializable;


public class BreadcrumbToolbarItem implements Serializable
{
    private static final long serialVersionUID = 1090124668787722373L;

    private String name;

    public BreadcrumbToolbarItem(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
