/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ass.Orders;

import java.sql.Date;

/**
 *
 * @author nttu2
 */
public class OrderDTO {

    private String id;
    private Date ordered_At;
    private double totatlPrice;
    private String status;
    private String u_id;

    public OrderDTO() {
    }

    public OrderDTO(String id, Date ordered_At, double totatlPrice, String status, String u_id) {
        this.id = id;
        this.ordered_At = ordered_At;
        this.totatlPrice = totatlPrice;
        this.status = status;
        this.u_id = u_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getOrdered_At() {
        return ordered_At;
    }

    public void setOrdered_At(Date ordered_At) {
        this.ordered_At = ordered_At;
    }

    public double getTotatlPrice() {
        return totatlPrice;
    }

    public void setTotatlPrice(double totatlPrice) {
        this.totatlPrice = totatlPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

}
