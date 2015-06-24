package com.android.porta.pk.responses;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Talha on 6/24/15.
 */
public class Product implements Parcelable {

    public static final String KEY = "product";

    public long product_id; /*43*/
    public String color; /*null*/
    public String size; /*null*/
    public String price; /*7000*/ // TODO change type
    public String sale_price; /*0*/ // TODO change type
    public String sale; /*0*/ // TODO is it boolean
    public String sale_date_start; /*0000-00-00 00:00:00*/
    public String sale_date_end; /*0000-00-00 00:00:00*/
    public long category_id; /*25*/
    public String product_name; /*Squatting Pan*/
    public String slug; /*squatting-pan_HD13*/
    public String code; /*HD13*/
    public String desc; /*Squatting Pan\nWithout Trap\nSize: 585x435x300mm*/
    public String short_desc; /**/
    public String date; /*2015-05-06 14:44:02*/
    public boolean is_new; /*0*/
    public boolean is_featured; /*0*/
    public boolean is_180; /*1*/
    public boolean is_on_sale; /*0*/
    public String status; /*1*/
    public int total_180_images; /*37*/
    public String category_name; /*Squatting Pan*/
    public String cat_slug; /*squatting-pan*/

    protected Product(Parcel in) {
        product_id = in.readLong();
        color = in.readString();
        size = in.readString();
        price = in.readString();
        sale_price = in.readString();
        sale = in.readString();
        sale_date_start = in.readString();
        sale_date_end = in.readString();
        category_id = in.readLong();
        product_name = in.readString();
        slug = in.readString();
        code = in.readString();
        desc = in.readString();
        short_desc = in.readString();
        date = in.readString();
        is_new = in.readByte() != 0x00;
        is_featured = in.readByte() != 0x00;
        is_180 = in.readByte() != 0x00;
        is_on_sale = in.readByte() != 0x00;
        status = in.readString();
        total_180_images = in.readInt();
        category_name = in.readString();
        cat_slug = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(product_id);
        dest.writeString(color);
        dest.writeString(size);
        dest.writeString(price);
        dest.writeString(sale_price);
        dest.writeString(sale);
        dest.writeString(sale_date_start);
        dest.writeString(sale_date_end);
        dest.writeLong(category_id);
        dest.writeString(product_name);
        dest.writeString(slug);
        dest.writeString(code);
        dest.writeString(desc);
        dest.writeString(short_desc);
        dest.writeString(date);
        dest.writeByte((byte) (is_new ? 0x01 : 0x00));
        dest.writeByte((byte) (is_featured ? 0x01 : 0x00));
        dest.writeByte((byte) (is_180 ? 0x01 : 0x00));
        dest.writeByte((byte) (is_on_sale ? 0x01 : 0x00));
        dest.writeString(status);
        dest.writeInt(total_180_images);
        dest.writeString(category_name);
        dest.writeString(cat_slug);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public void putSelf(Bundle args) {
        args.putParcelable(KEY, this);
    }

    public static Product get(Bundle args) {
        return args.getParcelable(KEY);
    }

    @Override
    public String toString() {
        return "Product{" +
                "product_id=" + product_id +
                ", color='" + color + '\'' +
                ", size='" + size + '\'' +
                ", price='" + price + '\'' +
                ", sale_price='" + sale_price + '\'' +
                ", sale='" + sale + '\'' +
                ", sale_date_start='" + sale_date_start + '\'' +
                ", sale_date_end='" + sale_date_end + '\'' +
                ", category_id=" + category_id +
                ", product_name='" + product_name + '\'' +
                ", slug='" + slug + '\'' +
                ", code='" + code + '\'' +
                ", desc='" + desc + '\'' +
                ", short_desc='" + short_desc + '\'' +
                ", date='" + date + '\'' +
                ", is_new=" + is_new +
                ", is_featured=" + is_featured +
                ", is_180=" + is_180 +
                ", is_on_sale=" + is_on_sale +
                ", status='" + status + '\'' +
                ", total_180_images=" + total_180_images +
                ", category_name='" + category_name + '\'' +
                ", cat_slug='" + cat_slug + '\'' +
                '}';
    }
}