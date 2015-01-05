package masterofgalaxy.gamestate.savegame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import masterofgalaxy.App;
import masterofgalaxy.MogGame;
import masterofgalaxy.assets.i18n.I18N;
import masterofgalaxy.exceptions.SavedGameException;

import java.io.*;
import java.util.UUID;

public class SaveGameStorage {
    public void save(MogGame game, String fileName) throws SavedGameException {
        FileHandle storageDir = getStorageDir();
        storageDir.mkdirs();
        FileHandle tempFileHandle = storageDir.child(UUID.randomUUID().toString());
        try {
            File tempFile = tempFileHandle.file();
            System.out.println("Writing save to temp file: " + tempFile.getPath());
            try {
                FileWriter writer = new FileWriter(tempFile);
                try {
                    GameSaver saver = new GameSaver(game, writer);
                    saver.save();
                } finally {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                throw new SavedGameException(I18N.resolve("$gameCannotBeSaved", e.getLocalizedMessage()));
            }

            FileHandle targetFile = storageDir.child(fileName);
            System.out.println("Moving save to destination file: " + targetFile.file().getPath());
            tempFileHandle.moveTo(targetFile);
        } finally {
            tempFileHandle.delete();
        }
    }

    public GameState load(String fileName) throws SavedGameException {
        File file = getStorageDir().child(fileName).file();
        try {
            FileReader reader = new FileReader(file);
            try {
                GameLoader loader = new GameLoader(reader);
                return loader.load();
            } finally {
                reader.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new SavedGameException(I18N.resolve("$gameCannotBeLoaded", I18N.resolve("$fileNotFound")));
        } catch (IOException e) {
            e.printStackTrace();
            throw new SavedGameException(I18N.resolve("$gameCannotBeLoaded", e.getLocalizedMessage()));
        }
    }

    private FileHandle getStorageDir() {
        return Gdx.files.external(App.externalStorageDir).child("saves");
    }
}
