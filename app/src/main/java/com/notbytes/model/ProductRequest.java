package com.notbytes.model;

public class ProductRequest
{
    String productId,leftDeviceId,rightDeviceId;

    public String getProductId()
    {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getLeftDeviceId() {
        return leftDeviceId;
    }

    public void setLeftDeviceId(String leftDeviceId) {
        this.leftDeviceId = leftDeviceId;
    }

    public String getRightDeviceId() {
        return rightDeviceId;
    }

    public void setRightDeviceId(String rightDeviceId) {
        this.rightDeviceId = rightDeviceId;
    }

    @Override
    public String toString()
    {
        return "ProductRequest{" +
                "productId='" + productId + '\'' +
                ", leftDeviceId='" + leftDeviceId + '\'' +
                ", rightDeviceId='" + rightDeviceId + '\'' +
                '}';
    }
}
