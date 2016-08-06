package melocha.vocabularykeeper;

import android.os.Parcel;
import android.os.Parcelable;

public class EngRusPair implements Parcelable{
    private int id;
    private String engWord;
    private String rusWord;

    public  EngRusPair(){}
    public  EngRusPair(String engWord,String rusWord){
        super();
        this.engWord = engWord;
        this.rusWord = rusWord;
    }

    public  EngRusPair(String engString, String rusWord, int id){
        super();
        this.engWord = engString;
        this.rusWord = rusWord;
        this.id = id;
    }

    public int getId(){return id; }

    public String getEngWord() {
        return engWord;
    }

    public void setEngWord(String engWord) {
        this.engWord = engWord;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRusWord(String rusWord) {
        this.rusWord = rusWord;
    }

    public String getRusWord() {
        return rusWord;
    }

    @Override
    public String toString() {
        String str = engWord + " - " + rusWord;
        return str;
    }



    //PARCELABLE

    private EngRusPair(Parcel source){
        this.id = source.readInt();
        this.rusWord = source.readString();
        this.engWord = source.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(engWord);
        dest.writeString(rusWord);
    }

    public static final Creator<EngRusPair> CREATOR
            = new Creator<EngRusPair>() {
        @Override
        public EngRusPair createFromParcel(Parcel source) {
            return new EngRusPair(source);
        }

        @Override
        public EngRusPair[] newArray(int size) {
            return new EngRusPair[size];
        }
    };
}
