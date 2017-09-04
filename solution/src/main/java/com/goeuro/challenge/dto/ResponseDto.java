package com.goeuro.challenge.dto;

/**
 * Created by jawad on 9/3/17.
 */
public class ResponseDto {
    String dep_sid;
    String arr_sid;
    boolean direct_bus_route;

    public ResponseDto(String dep_sid, String arr_sid) {
        this.dep_sid = dep_sid;
        this.arr_sid = arr_sid;
    }

    public String getDep_sid() {
        return dep_sid;
    }

    public void setDep_sid(String dep_sid) {
        this.dep_sid = dep_sid;
    }

    public String getArr_sid() {
        return arr_sid;
    }

    public void setArr_sid(String arr_sid) {
        this.arr_sid = arr_sid;
    }

    public boolean isDirect_bus_route() {
        return direct_bus_route;
    }

    public void setDirect_bus_route(boolean direct_bus_route) {
        this.direct_bus_route = direct_bus_route;
    }
}
