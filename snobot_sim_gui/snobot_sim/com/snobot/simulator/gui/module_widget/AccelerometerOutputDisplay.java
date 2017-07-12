package com.snobot.simulator.gui.module_widget;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Collection;
import java.util.Map.Entry;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.snobot.simulator.gui.module_widget.settings.SimpleSettingsDialog;
import com.snobot.simulator.jni.module_wrapper.AccelerometerWrapperJni;

public class AccelerometerOutputDisplay extends BaseWidgetDisplay<Integer, AccelerometerDisplay>
{

    public AccelerometerOutputDisplay(Collection<Integer> aKeys)
    {
        super(aKeys);
        setBorder(new TitledBorder("Acceleromoters"));
    }

    @Override
    public void update()
    {
        for (Entry<Integer, AccelerometerDisplay> pair : mWidgetMap.entrySet())
        {
            if (pair.getValue().isVisible())
            {
                double accel = AccelerometerWrapperJni.getAcceleration(pair.getKey());
                pair.getValue().updateDisplay(accel);
            }
        }
    }

    @Override
    protected AccelerometerDisplay createWidget(Integer aKey)
    {
        return new AccelerometerDisplay();
    }

    @Override
    protected JDialog createSettingsDialog(Integer aKey)
    {
        SimpleSettingsDialog dialog = new SimpleSettingsDialog("Accelerometer " + aKey + " Settings", aKey, getName(aKey))
        {

            @Override
            protected void onSubmit()
            {
                AccelerometerWrapperJni.setName(aKey, getComponentName());
                mLabelMap.get(aKey).setText(getComponentName());
            }
        };

        dialog.pack();

        return dialog;
    }

    @Override
    protected String getName(Integer aKey)
    {
        return AccelerometerWrapperJni.getName(aKey);
    }
}

class AccelerometerDisplay extends JPanel
{
    private static final int sWIDTH = 80;
    private static final int sHEIGHT = 15;

    private double mAcceleration;
    private double mMaxAcceleration;

    public AccelerometerDisplay()
    {
        setPreferredSize(new Dimension(sWIDTH, sHEIGHT));
        mMaxAcceleration = 2;
    }

    public void updateDisplay(double aAccel)
    {
        mAcceleration = aAccel;
    }

    @Override
    public void paint(Graphics g)
    {
        g.clearRect(0, 0, getWidth(), getHeight());

        int halfway = getWidth() / 2;

        int offset = (int) (Math.abs(mAcceleration) / mMaxAcceleration * halfway);

        // Draw some indicator even though it isn't moving
        if (offset == 0)
        {
            offset = 1;
        }

        g.setColor(Color.black);
        g.drawRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.blue);
        if (mAcceleration < 0)
        {
            g.fillRect(halfway - offset, 0, offset, getHeight());
        }
        else
        {
            g.fillRect(halfway, 0, offset, getHeight());
        }
    }
}