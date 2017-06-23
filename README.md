# FreeRouting

Java Based Printed Circuit Board Routing Software from FreeRouting.net written by Alfons Wirtz.

## Introduction

This software can be used together with all host PCB design software systems containing a standard Specctra or Electra DSN interface. It imports .dsn-files generated by the Specctra interface of the host system and exports Specctra session files.(There exists also an interface to Cadsoft-Eagle.)

There are three modes for routing traces: 90 degree, 45 degree and free angle. The interactive router is production stable and unsurpassed in its free angle capabilities. An autorouter is currently under development and already stable in the conventional 45 degree mode.

After launching the router a window appears with buttons to display some router demonstrations, to open a sample design, or to open a design of your own.

After opening a design you can start the autorouter with the button in the toolbar on top of the board window.

The board editor has three different interactive states. You can switch between this states with the buttons Select, Route and Drag on the left of the toolbar.

In the beginning the board editor is in the select state. In this state you can select single board items by picking them with the left mouse button or select items in a rectangle by dragging the left mouse button. Only item types switched on in the select parameter sheet will be selected. After selecting some items the toolbar displays options for showing and manipulating these items. If you push the info button for example a window with text information about the selected items is displayed. After clicking a blue word in this text a new window with further information pops up. To return to the select state push the cancel button or click somewhere in the empty space of the board window.

By pushing the Route button you get into the state for interactive routing. In this state you can start a new trace by picking an item belonging to a net, for example a pin. Then you can follow the displayed airline with the mouse until you have reached the target item at the other end of the airline. The trace will be connected automatically to the target, if it is on the same layer. If you want to change to a different layer during interactive routing, select "change layer" and then the name of the new layer in the popup menu under the right mouse button. Then a via will be inserted, if that is possible, and a new trace starts on the new layer. You can also change the layer by pressing a number key.

After pushing the Drag button you get into the state for changing the location of vias, components or traces. In this state you can select vias or components and drag them with the left mouse button to a different location. The connected route is updated automatically. You can also move traces by pushing them from behind out of the empty space with the left mouse button pressed. That works on the current layer, which can be changed in the select parameter sheet. In this way you can make space for example to insert a new component.

For more information please use the bundled help from the board editor.

## Additional steps for users of CadSoft-Eagle

1. Download the latest Eagle2freerouter ulp file
2. Start Eagle and open in the control panel of Eagle for example the design my_design.brd.
3. Choose in the Files pulldown-menu of Eagle the item "execute ULP" and select the Eagle2freerouter ulp file. A file with name my_design.dsn is generated.
4. Start the router, push the "Open Your Own Design" button and select my_design.dsn in the file chooser.
5. After making some changes to the design with the router select "export Eagle session script" in the Files pulldown-menu. A file with name my_design.scr is generated.
6. Choose in the Files pulldown-menu of Eagle the item "execute Script" and select my_design.scr.


## Here are some instructions how to run the Freerouting project in the NetBeans IDE

1. Go to the Java EA download web page and download and install JDK 9 EA http://jdk.java.net/9/, and download Development version of NetBeans IDE http://bits.netbeans.org/download/trunk/nightly/latest/
2. Start the NetBeans IDE and select File | Open Project in the pull down menu and select freeroute project.
3. Build the project. The router should run now.

For optional parameters use -h argument.

## Here are some instructions how to build the Freerouting project from a Terminal

### Fedora

Install maven
```bash
sudo dnf install maven
```

Install Oracle JDK & JRE 9 EA http://jdk.java.net/9/
```bash
tar xzvf jdk-9-ea+170_linux-x64_bin.tar.gz -C /usr/java
su -c chown -R root:root /usr/java
su -c chown -R +x /usr/java/bin
su -c pdate-alternatives --install /usr/bin/java java /usr/java/jdk1.9*/bin/java 1065
su -c update-alternatives --install /usr/bin/javac javac /usr/java/jdk1.9*/bin/javac 1065
su -c update-alternatives --install /usr/bin/jar jar /usr/java/jdk1.9*/bin/jar 1065
su -c update-alternatives --install /usr/bin/javaws javaws /usr/java/jdk1.9*/bin/javaws 1065
su -c update-alternatives --config java
```

Download freeroute source
```bash
git clone https://github.com/rbuj/FreeRouting.git
```

Build jar
```bash
cd FreeRouting
mvn install
```

Run freeroute
```bash
cd target
export J2D_TRACE_LEVEL=4
java -cp freeroute-[version].jar:libext/javahelp-2.0.05.jar:libext/guava-22.0.jar net.freerouting.freeroute.MainApp
(or)
java -jar freeroute-[version].jar
(or)
java -jar freeroute-[version]-jar-with-dependencies.jar
```

### OS X
Download and install Oracle's JDK & JRE 9. http://jdk.java.net/9/

Install Apache Maven. https://maven.apache.org/

Download freeroute source
```bash
git clone https://github.com/rbuj/FreeRouting.git
```

Build jar
```bash
cd FreeRouting
mvn install
```

Run freeroute
```bash
cd target
export J2D_TRACE_LEVEL=4
java -cp freeroute-[version].jar:libext/javahelp-2.0.05.jar:libext/guava-22.0.jar net.freerouting.freeroute.MainApp
(or)
java -jar freeroute-[version].jar
(or)
java -jar freeroute-[version]-jar-with-dependencies.jar
```
