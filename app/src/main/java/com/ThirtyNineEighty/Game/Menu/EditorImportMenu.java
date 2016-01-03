package com.ThirtyNineEighty.Game.Menu;

import com.ThirtyNineEighty.Base.Menus.BaseMenu;
import com.ThirtyNineEighty.Base.Menus.Selector;
import com.ThirtyNineEighty.Game.Common.EditorExporter;
import com.ThirtyNineEighty.Game.Common.LoadException;
import com.ThirtyNineEighty.Base.Menus.Controls.Button;
import com.ThirtyNineEighty.Base.Subprogram;
import com.ThirtyNineEighty.Base.Worlds.IWorld;
import com.ThirtyNineEighty.Base.Renderable.GL.GLLabel;
import com.ThirtyNineEighty.Base.Resources.MeshMode;
import com.ThirtyNineEighty.Game.TanksContext;

public class EditorImportMenu
  extends BaseMenu
{
  private Selector mapSelector;
  private GLLabel messageLabel;
  private GLLabel mapNameLabel;

  @Override
  public void initialize()
  {
    super.initialize();

    messageLabel = new GLLabel(" ", MeshMode.Dynamic);
    messageLabel.setPosition(0, -100);
    messageLabel.setVisible(false);
    bind(messageLabel);

    Button menuButton = new Button("Back");
    menuButton.setPosition(-710, 465);
    menuButton.setSize(500, 150);
    menuButton.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        TanksContext.content.setMenu(new EditorMenu());
      }
    });
    add(menuButton);

    Button apply = new Button("Load");
    apply.setPosition(710, 465);
    apply.setSize(500, 150);
    apply.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        try
        {
          IWorld world = TanksContext.content.getWorld();
          EditorExporter.importMap(world, mapSelector.getCurrent());
          TanksContext.content.setMenu(new EditorMenu());
        }
        catch (LoadException e)
        {
          showMessage(e.getMessage());
        }
      }
    });
    add(apply);

    mapSelector = new Selector(EditorExporter.getMaps(), new Selector.Callback()
    {
      @Override
      public void onChange(String current)
      {
        mapNameLabel.setValue(current);
      }
    });

    mapNameLabel = new GLLabel(mapSelector.getCurrent(), MeshMode.Dynamic);
    bind(mapNameLabel);

    Button prevMapButton = new Button("Prev map");
    prevMapButton.setPosition(-160, -220);
    prevMapButton.setSize(300, 200);
    prevMapButton.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        mapSelector.Prev();
      }
    });
    add(prevMapButton);

    Button nextMapButton = new Button("Next map");
    nextMapButton.setPosition(160, -220);
    nextMapButton.setSize(300, 200);
    nextMapButton.setClickListener(new Runnable()
    {
      @Override
      public void run()
      {
        mapSelector.Next();
      }
    });
    add(nextMapButton);
  }

  private void showMessage(String message)
  {
    messageLabel.setVisible(true);
    messageLabel.setValue(message);

    bind(new Subprogram()
    {
      boolean delayed;

      @Override
      protected void onUpdate()
      {
        if (!delayed)
        {
          delay(5000);
          delayed = true;
          return;
        }

        messageLabel.setVisible(false);
        unbind();
      }
    });
  }
}
