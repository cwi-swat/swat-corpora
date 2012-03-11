<?xml version="1.0" encoding="ASCII"?>
<tm.widgets:Composite xmi:version="2.0" xmlns:xmi="http://www.omg.org/XMI" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:tm.layouts="http://www.eclipse.org/e4/tm/layouts.ecore" xmlns:tm.widgets="http://www.eclipse.org/e4/tm/widgets.ecore" name="aComposite">
  <controls xsi:type="tm.widgets:Label" name="aLabel" text="A label"/>
  <controls xsi:type="tm.widgets:Text" name="aText" text="A text field">
    <layoutData xsi:type="tm.layouts:RectangleLayoutData" x="100" y="10"/>
  </controls>
  <controls xsi:type="tm.widgets:PushButton" name="aPushButton" text="A push button">
    <layoutData xsi:type="tm.layouts:RectangleLayoutData" y="40"/>
  </controls>
  <controls xsi:type="tm.widgets:ToggleButton" name="aToggleButton" text="A toggle button">
    <layoutData xsi:type="tm.layouts:RectangleLayoutData" x="150" y="40"/>
  </controls>
  <layout xsi:type="tm.layouts:RectangleLayout">
    <defaultLayoutData width="20" height="40"/>
  </layout>
</tm.widgets:Composite>
