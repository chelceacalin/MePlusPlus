package com.example.meplusplus.ToDo;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class WriteToFileClass {


    public static void write(Context context, ArrayList<String> list, String numefisier) {
        ObjectOutputStream outputStream;
       try (FileOutputStream fileOutputStream = context.openFileOutput(numefisier, Context.MODE_PRIVATE)) {
           outputStream  = new ObjectOutputStream(fileOutputStream);

            for (String item : list) {
                outputStream.writeUTF(item);
            }
           outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public  static ArrayList<String> read(Context context, String numefisier) {
        ArrayList<String> list = new ArrayList<>();

        try (FileInputStream fileInputStream = context.openFileInput(numefisier)) {
            ObjectInputStream inputStream = new ObjectInputStream(fileInputStream);

            while (true) {
                String str = inputStream.readUTF();
                list.add(str);
            }

        } catch (IOException e) {
            return list;
        }

    }
}

