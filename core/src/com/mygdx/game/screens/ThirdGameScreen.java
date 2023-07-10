package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.game.MyGdxGame;
import com.mygdx.game.actorsGame3.Item;
import com.mygdx.game.ui.ImageView;
import com.mygdx.game.ui.TextView;
import com.mygdx.game.ui.UiComponent;
import com.mygdx.game.ui.WhiteRectangle;
import com.mygdx.game.utils.GameSettings;
import com.mygdx.game.utils.MemoryLoader;

import java.util.ArrayList;
import java.util.Random;

public class ThirdGameScreen implements Screen {
    MyGdxGame myGdxGame;
    ArrayList<UiComponent> components;
    ArrayList<Item> itemsComponents;
    ArrayList<UiComponent> itemsUIcomponents;
    ArrayList<UiComponent> uiComponentsEndOfGame;
    ImageView rightIcon;
    int returnMenuWidth = (int) (GameSettings.SCR_WIDTH * 0.6);
    int returnMenuHeight = (int) (GameSettings.SCR_HEIGHT * 0.1);
    int rightIconBgWidth = (int) (GameSettings.SCR_WIDTH * 0.2);
    int rightIconBgHeight = (int) (GameSettings.SCR_HEIGHT * 0.1);
    // ТАЙМЕР ОБРАТНОГО ОТСЧЕТА ПРИ ПЕРЕХОДЕ В GAME OVER
    private float timer = 30f;
    final int borderPosition = (int) (GameSettings.SCR_HEIGHT * 0.15);
    int XP = 0;
    private Timer.Task createObjectTask;
    private float intervalTimer = 0f;
    boolean isGameFinished = false;
    TextView hpText;
    TextView timerExpires;
    WhiteRectangle whiteRect;
    boolean isClickableFinishButtons = false;

    public ThirdGameScreen(MyGdxGame myGdxGame) {
        this.myGdxGame = myGdxGame;

        components = new ArrayList<>();
        itemsComponents = new ArrayList<>();
        itemsUIcomponents = new ArrayList<>();

        whiteRect = new WhiteRectangle(myGdxGame);
        whiteRect.initRestartButton(onClickBtnRestart);
        whiteRect.initReturnMenu(onClickBtnReturn);
        uiComponentsEndOfGame = whiteRect.getComponents();

        ImageView returnMenu = new ImageView(0, GameSettings.SCR_HEIGHT - returnMenuHeight, returnMenuWidth, returnMenuHeight, "images/chooseright.png");
        TextView clickText = new TextView(myGdxGame.gameFontLarge1.bitmapFont, "Click!", GameSettings.SCR_WIDTH / 2 - 150, 200);
        ImageView bg = new ImageView(0, 0, GameSettings.SCR_WIDTH, GameSettings.SCR_HEIGHT, "backgrounds/bg3.png");
        ImageView border = new ImageView(0, borderPosition, GameSettings.SCR_WIDTH, 10, "images/border.png");
        ImageView rightTopBg = new ImageView(GameSettings.SCR_WIDTH - rightIconBgWidth, GameSettings.SCR_HEIGHT - rightIconBgHeight, rightIconBgWidth, rightIconBgHeight, "images/right_top_bg_game3.png");
        hpText = new TextView(myGdxGame.gameFontLarge2.bitmapFont, String.valueOf(XP), 50, 200);
        timerExpires = new TextView(myGdxGame.gameFontLarge2.bitmapFont, Float.toString(timer), 50, GameSettings.SCR_HEIGHT - rightIconBgHeight - 50);

        components.add(bg);
        components.add(rightTopBg);
        components.add(returnMenu);
        components.add(clickText);
        components.add(border);
        components.add(hpText);

        returnMenu.setOnClickListener(onClickBtnReturn);
    }

    public void initItems(int i) {
        int itemNum = new Random().nextInt(2);
        String itemTitle = itemNum == 1 ? "apple" : "ball";
        Texture texture = new Texture("icons/game3/" + itemTitle + ".png");
        final Item element = new Item(texture, 100 * (i % 2 + 1) + new Random().nextInt(GameSettings.SCR_WIDTH - 200 * (i % 2 + 1)), 4 * borderPosition + new Random().nextInt(GameSettings.SCR_HEIGHT - 5 * borderPosition), itemNum, onKillItemListener);

        element.actorImgView.setOnClickListener(new UiComponent.OnClickListener() {
            @Override
            public void onClick() {
                if (element.isActive) {
                    if (element.getTypeItem() == 1) XP++;
                    else XP--;
                    element.isActive = false;
                    element.actorImgView.y = -element.height;
                    hpText.setText(String.valueOf(XP));
                }
            }
        });

        itemsComponents.add(element);
        itemsUIcomponents.add(element.actorImgView);
    }

    public void generateItems() {
        for (int i = 0; i < 6; i++) initItems(i);
        createObjectTask = new Timer.Task() {
            @Override
            public void run() {
                if (!isGameFinished) for (int i = 0; i < 3; i++) initItems(i);
            }
        };

        Timer.schedule(createObjectTask, 1f, 1f);
    }
    @Override
    public void show() {
        rightIcon = new ImageView(GameSettings.SCR_WIDTH - rightIconBgWidth, GameSettings.SCR_HEIGHT - rightIconBgHeight, rightIconBgWidth, rightIconBgHeight, "icons/icon" + MemoryLoader.loadIconState() + ".png");
        components.add(rightIcon);
        generateItems();
        components.add(timerExpires);
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.justTouched()) {
            myGdxGame.touch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            myGdxGame.camera.unproject(myGdxGame.touch);
            if (!isGameFinished) {
                for (UiComponent component : components) {
                    if (component.isVisible)
                        component.isHit((int) myGdxGame.touch.x, (int) myGdxGame.touch.y);
                }
                for (UiComponent component : itemsUIcomponents) {
                    if (component.isVisible)
                        component.isHit((int) myGdxGame.touch.x, (int) myGdxGame.touch.y);
                }
            }
            for (UiComponent component : uiComponentsEndOfGame) {
                if (component.isVisible) component.isHit((int) myGdxGame.touch.x, (int) myGdxGame.touch.y);
            }
        }

        if (!isGameFinished) {
            initMainTimer();
            initGenerateItemsTimer();
        }

        for (Item item: itemsComponents) {
            if (!isGameFinished) {
                item.update();
                if (item.getY() < borderPosition) item.isActive = false;
            }
        }

        ScreenUtils.clear(0.95686274509f, 0.95686274509f, 0.95686274509f, 1);
        myGdxGame.camera.update();
        myGdxGame.batch.begin();
        myGdxGame.batch.setProjectionMatrix(myGdxGame.camera.combined);

        for (UiComponent component: components) {
            if (component.isVisible) component.draw(myGdxGame.batch);
        }
        for (UiComponent component: itemsUIcomponents) {
            if (component.isVisible) component.draw(myGdxGame.batch);
        }

        if (isGameFinished) {
            for (UiComponent component: uiComponentsEndOfGame) {
                component.draw(myGdxGame.batch);
            }
        }

        myGdxGame.batch.end();
    }

    public void initGenerateItemsTimer() {
        intervalTimer += Gdx.graphics.getDeltaTime();
        if (intervalTimer >= 3f) {
            intervalTimer -= 3f;
            createObjectTask.run();
        }
    }

    public void initMainTimer() {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                timerExpires.setText(String.valueOf((int) timer));
            }
        }, 1, 1);

        timer -= Gdx.graphics.getDeltaTime();
        if (timer < 0) {
            timer = 0;
            Timer.instance().clear();
            whiteRect.setResult(String.valueOf(XP));
            isGameFinished = true;
            Timer.instance().scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    isClickableFinishButtons = true;
                }
            }, 2);
        }
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
        clearData();
    }

    @Override
    public void dispose() {
        Timer.instance().clear();
    }

    public void clearData() {
        for (Item component: itemsComponents) {
            component.actorImgView.imgTexture.dispose();
        }
        for (UiComponent component: itemsUIcomponents) {
            component.isVisible = false;
        }
        itemsComponents.clear();
        timer = 30f;
        XP = 0;
        intervalTimer = 0f;
        isClickableFinishButtons = false;
        Timer.instance().clear();
        isGameFinished = false;
    }
    UiComponent.OnClickListener onClickBtnReturn = new UiComponent.OnClickListener() {
        @Override
        public void onClick() {
            clearData();
            myGdxGame.setScreen(myGdxGame.menuScreen);
        }
    };

    Item.OnKillItemListener onKillItemListener = new Item.OnKillItemListener() {
        @Override
        public void onKill() {
            Gdx.app.debug("onKill", "killed");
        }
    };

    UiComponent.OnClickListener onClickBtnRestart = new UiComponent.OnClickListener() {
        @Override
        public void onClick() {
            if (isClickableFinishButtons) {
                clearData();
                generateItems();
            }
        }
    };
}