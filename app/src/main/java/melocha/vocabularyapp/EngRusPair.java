package melocha.vocabularyapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Admin on 12.03.2015.
 */
public class EngRusPair implements Parcelable{
    private int id;
    private String engWord;
    private ArrayList<String> rusWords;

    public  EngRusPair(){}
    public  EngRusPair(String engString, ArrayList<String> rusStrings){
        super();
        this.engWord = engString;
        this.rusWords = rusStrings;
    }

    public  EngRusPair(String engString, String rusStrings, int id){
        super();
        this.engWord = engString;
        this.rusWords =  new ArrayList<String>(Arrays.asList(rusStrings.trim().split("[;]+")));
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

    public void setRusWords(ArrayList<String> rusWords) {
        this.rusWords = rusWords;
    }

    public ArrayList<String> getRusWords() {
        return rusWords;
    }

    @Override
    public String toString() {
        String str = engWord + " - ";
        for (int i = 0; i < rusWords.size(); i++){
            str += rusWords.get(i) + "; ";
        }
        return str;
    }

    public String rusWordsToString(){
        String str = new String();
        for (int i = 0; i < rusWords.size(); i++){
            str += rusWords.get(i) + "; ";
        }
        return str;
    }


    //todo parcelable

    private EngRusPair(Parcel source){
        this.id = source.readInt();
        source.readStringList(this.rusWords);
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
        dest.writeStringList(rusWords);
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
