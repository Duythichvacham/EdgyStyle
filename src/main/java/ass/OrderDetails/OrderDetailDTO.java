/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ass.OrderDetails;

/**
 *
 * @author nttu2
 */
public class OrderDetailDTO {
    private int o_id;
    private int p_id;
    private int quantity;
    private double price_At;

    public OrderDetailDTO() {
    }

    public OrderDetailDTO(int o_id, int p_id, int quantity, double price_At) {
        this.o_id = o_id;
        this.p_id = p_id;
        this.quantity = quantity;
        this.price_At = price_At;
    }

    public int getO_id() {
        return o_id;
    }

    public void setO_id(int o_id) {
        this.o_id = o_id;
    }

    public int getP_id() {
        return p_id;
    }

    public void setP_id(int p_id) {
        this.p_id = p_id;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice_At() {
        return price_At;
    }

    public void setPrice_At(double price_At) {
        this.price_At = price_At;
    }

   
    
}
