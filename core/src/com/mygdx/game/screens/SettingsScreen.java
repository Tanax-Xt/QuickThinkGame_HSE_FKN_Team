package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.ui.ImageView;
import com.mygdx.game.ui.UiComponent;
import com.mygdx.game.utils.GameSettings;
import com.mygdx.game.utils.MemoryLoader;

import java.util.ArrayList;

public class SettingsScreen implements Screen {
    MyGdxGame myGdxGame;
    ArrayList<UiComponent> components;
    ArrayList<ImageView> iconsList;
    int returnMenuWidth = (int) (GameSettings.SCR_WIDTH * 0.6);
    int returnMenuHeight = (int) (GameSettings.SCR_HEIGHT * 0.1);
    int bgSettingsHeight = (int) (GameSettings.SCR_HEIGHT * 0.4);
    int settingsIconWidth = (int) (GameSettings.SCR_WIDTH * 0.2);
    int settingsIconHeight = (int) (GameSettings.SCR_HEIGHT * 0.1);
    int iconWidth = (int) (GameSettings.SCR_WIDTH * 0.28);
    int iconHeight = (int) (GameSettings.SCR_HEIGHT * 0.15);

    public SettingsScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;

        components = new ArrayList<>();
        iconsList = new ArrayList<>();

        ImageView bgSettings = new ImageView(0, GameSettings.SCR_HEIGHT - bgSettingsHeight, GameSettings.SCR_WIDTH, bgSettingsHeight, "backgrounds/settingsbg.png");
        ImageView returnMenu = new ImageView(0, GameSettings.SCR_HEIGHT - returnMenuHeight, returnMenuWidth, returnMenuHeight, "buttons/settingsReturn.png");
        ImageView settingsIcon = new ImageView(GameSettings.SCR_WIDTH - settingsIconWidth, GameSettings.SCR_HEIGHT - settingsIconHeight, settingsIconWidth, settingsIconHeight, "icons/settings.png");

        components.add(bgSettings);
        components.add(returnMenu);
        components.add(settingsIcon);

        int activeIcon = MemoryLoader.loadIconState();
        for (int i = 1; i <= 3; i++) {
            if (activeIcon == i) {
                ImageView activeIconImage = new ImageView((int) (GameSettings.SCR_WIDTH / 2 - iconWidth * 0.7), GameSettings.SCR_HEIGHT / 2 + iconHeight, (int) (iconWidth * 1.5), (int) (iconHeight * 1.5), "icons/icon" + i + ".png");
                components.add(activeIconImage);
            }
            ImageView icon = new ImageView((int) (i * 50 + (i - 1) * (GameSettings.SCR_WIDTH * 0.27)), (int) (0.95 * GameSettings.SCR_HEIGHT - bgSettingsHeight - iconHeight), iconWidth, iconHeight, "icons/icon" + i + ".png");
//            icon.setOnClickListener();
            components.add(icon);
        }
        for (int i = 4; i <= 6; i++) {
            if (activeIcon == i) {
                ImageView activeIconImage = new ImageView((int) (GameSettings.SCR_WIDTH / 2 - iconWidth * 0.7), GameSettings.SCR_HEIGHT / 2 + iconHeight, (int) (iconWidth * 1.5), (int) (iconHeight * 1.5), "icons/icon" + i + ".png");
                components.add(activeIconImage);
            }
            ImageView icon = new ImageView((int) ((i - 3) * 50 + (i - 4) * (GameSettings.SCR_WIDTH * 0.27)), (int) (0.95 * GameSettings.SCR_HEIGHT - bgSettingsHeight - 2.2 * iconHeight), iconWidth, iconHeight, "icons/icon" + i + ".png");
            components.add(icon);
        }

        bgSettings.setOnClickListener(onClickBtnReturn);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (Gdx.input.justTouched()) {
            myGdxGame.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            myGdxGame.camera.unproject(myGdxGame.touch);
            for (UiComponent component : components) {
                if (component.isVisible) component.isHit((int) myGdxGame.touch.x, (int) myGdxGame.touch.y);
            }
        }

        ScreenUtils.clear(1, 1, 1, 1);
        myGdxGame.camera.update();
        myGdxGame.batch.begin();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);
        for (UiComponent component: components) {
            component.draw(myGdxGame.batch);
        }

        myGdxGame.batch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    UiComponent.OnClickListener onClickBtnReturn = new UiComponent.OnClickListener() {
        @Override
        public void onClick() {
            myGdxGame.setScreen(myGdxGame.menuScreen);
        }
    };

    UiComponent.OnClickListener onClickBtnChooseItem = new UiComponent.OnClickListener() {
        @Override
        public void onClick() {
//            MemoryLoader.saveIconState();
        }
    };
}
