package com.nectar.nectaronline;

public class ModeL_Cart_Items {

    public String name;//name of the product
    public String brand;//brand/producer of the product
    public String id;
    public String finalPrice;
    public String initialPrice;
    public String description;
    public String keyFeatures;
    public String specification;
    public String colour;
    public String size;
    public String weight;
    public String material;
    public String insideBox;
    public String warranty;
    public String instock;
    public String state;
    public String images;
    public String sellerID;
    public String productID;
    public String quantity;

    public ModeL_Cart_Items(String name, String brand, String id, String finalPrice, String initialPrice, String description, String keyFeatures, String specification, String colour, String size, String weight, String material, String insideBox, String warranty, String instock, String state, String images, String sellerID, String productID, String quantity) {
        this.name = name;
        this.brand = brand;
        this.id = id;
        this.finalPrice = finalPrice;
        this.initialPrice = initialPrice;
        this.description = description;
        this.keyFeatures = keyFeatures;
        this.specification = specification;
        this.colour = colour;
        this.size = size;
        this.weight = weight;
        this.material = material;
        this.insideBox = insideBox;
        this.warranty = warranty;
        this.instock = instock;
        this.state = state;
        this.images = images;
        this.sellerID = sellerID;
        this.productID = productID;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getId() {
        return id;
    }

    public String getFinalPrice() {
        return finalPrice;
    }

    public String getInitialPrice() {
        return initialPrice;
    }

    public String getDescription() {
        return description;
    }

    public String getKeyFeatures() {
        return keyFeatures;
    }

    public String getSpecification() {
        return specification;
    }

    public String getColour() {
        return colour;
    }

    public String getSize() {
        return size;
    }

    public String getWeight() {
        return weight;
    }

    public String getMaterial() {
        return material;
    }

    public String getInsideBox() {
        return insideBox;
    }

    public String getWarranty() {
        return warranty;
    }

    public String getInstock() {
        return instock;
    }

    public String getState() {
        return state;
    }

    public String getImages() {
        return images;
    }

    public String getSellerID() {
        return sellerID;
    }

    public String getProductID() {
        return productID;
    }

    public String getQuantity() {
        return quantity;
    }
}
