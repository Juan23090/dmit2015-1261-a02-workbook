## Week 7 – Jakarta Data

### Lesson 17–18: Jakarta Data, Autocomplete, and Leaflet

In these lessons, we connect to an Oracle database, generate JPA entities from existing tables, and use Leaflet.js to display interactive maps in Jakarta Faces.

---

### Lesson 17: Jakarta Data and Autocomplete

#### 1. Start Oracle Database

Start the Oracle container on the course Ubuntu Desktop VM:

```bash
podman start dmit2015-oracle-free-lite
```

View the container logs:

```bash
podman logs dmit2015-oracle-free-lite
```

Check its status:

```bash
podman ps
```

**What it does:** Starts the Oracle database for the Jakarta Data demonstration.

#### 2. Configure Oracle Database Connection — Slide 12

In IntelliJ IDEA:

```text
Database → New → Data Source → Oracle
```

**Connection settings:**

```text
Name:             OracleHrSchema@localhost
Host:             localhost
Port:             1521
Connection type:  Service name
Service:          FREEPDB1
User:             HR
Password:         Password2015
```

The **Human Resources (HR)** schema is used for the demonstration. The **Customer Orders (CO)** schema uses the same Oracle server with a different database account.

**What it does:** Connects IntelliJ to Oracle to explore tables, relationships, and database records.

#### 3. Generate JPA Entities from Database — Slides 16–17

In IntelliJ's Database window:

```text
Oracle HR Schema
→ Tables
→ Right-click
→ Create JPA Entities from DB
```

**Generation settings:**

```text
Target package: dmit2015.entity
References:     Enabled
```

Select the tables you want to generate as JPA entities.

**What it does:** Generates Java entity classes from existing Oracle tables, including JPA annotations and relationships based on foreign keys.

> **Assignment 4 uses this database-first workflow:** generate JPA entities from existing database tables.

---

### Lesson 18: Autocomplete, and Leaflet Maps

Follow the code and lesson instructions in:

```text
dmit2015-1261-jakarta-data-hr-demo-main/
```

**Leaflet documentation:** https://leafletjs.com

Go to **Tutorials → Quick Start Guide**.

#### 1. Create the Leaflet Quick Guide Page

Create:

```text
src/main/webapp/leaflet-js-quick-guide.xhtml
```

**Template:**

```text
DMIT2015 Faces Minimal Composition Page (No Scaffolding)
```

**Template values:**

```text
Filename: leaflet-js-quick-guide
Heading:  Leaflet Quick Guide
```

**What it does:** Creates a Jakarta Faces page for displaying a basic Leaflet map.

#### 2. Add Leaflet CSS and JavaScript

In `leaflet-js-quick-guide.xhtml`, add the Leaflet stylesheet:

```xhtml
<ui:define name="externalstyles">
    <link rel="stylesheet"
          href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css"
          integrity="sha256-p4NxAoJBhIIN+hmNHrzRCf9tD/miZyoHS5obTRR9BMY="
          crossorigin=""/>
</ui:define>
```

Add the Leaflet JavaScript after the CSS:

```xhtml
<ui:define name="headscripts">
    <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"
            integrity="sha256-20nQCchB9co0qIjJZRGuk2/Z9VM+kNiyxNV1lvTlZBo="
            crossorigin=""></script>
</ui:define>
```

The shared `layout.xhtml` provides the `externalstyles` and `headscripts` sections.

**What it does:** Loads the Leaflet CSS and JavaScript libraries.

#### 3. Add the Map Container

Inside `maincontent`, remove the generated form and keep the heading.

Add:

```html
<div id="map" style="height: 180px"></div>
```

**What it does:** Creates the HTML container where Leaflet displays the map.

#### 4. Initialize the Leaflet Map

Inside `leaflet-js-quick-guide.xhtml`, add:

```xhtml
<ui:define name="bodyscripts">
    <h:outputScript>
        var map = L.map('map').setView([51.505, -0.09], 13);

        L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
            maxZoom: 19,
            attribution: '&#169; OpenStreetMap'
        }).addTo(map);
    </h:outputScript>
</ui:define>
```

**What it does:** Initializes the map using Leaflet and OpenStreetMap tiles.

#### 5. Add the Page to Navigation

Open:

```text
src/main/webapp/WEB-INF/faces-templates/layout.xhtml
```

Add:

```xhtml
<p:menuitem value="Leaflet Demo"
            outcome="/leaflet-js-quick-guide"/>
```

**What it does:** Adds a navigation link to the Leaflet demonstration page.

---

#### 6. Create the MapLocation Model

Create:

```text
src/main/java/dmit2015/model/MapLocation.java
```

```java
package dmit2015.model;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
public class MapLocation {

    private String id;
    private String storeName;
    private double latitude;
    private double longitude;

    public MapLocation(String storeName, double latitude, double longitude) {
        id = UUID.randomUUID().toString();
        this.storeName = storeName;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
```

**What it does:** Stores each location's unique ID, name, latitude, and longitude.

#### 7. Create the MapLocation Backing Bean

Create:

```text
src/main/java/dmit2015/view/MapLocationView.java
```

```java
package dmit2015.view;

import dmit2015.model.MapLocation;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import lombok.Getter;

import java.util.List;

@Named
@RequestScoped
public class MapLocationView {

    @Getter
    private List<MapLocation> locations = List.of(
        new MapLocation(
            "Northern Alberta Institute of Technology",
            53.568072, -113.504224
        ),
        new MapLocation(
            "University of Alberta",
            53.5231733, -113.5275665
        ),
        new MapLocation(
            "MacEwan University",
            53.5470785, -113.5101388
        )
    );
}
```

**What it does:** Provides sample locations for the Jakarta Faces page.

#### 8. Create the Multiple Locations Page

Create:

```text
src/main/webapp/leaflet-map-locations.xhtml
```

**Template:**

```text
DMIT2015 Faces Minimal Composition Page (No Scaffolding)
```

**Template values:**

```text
Filename: leaflet-map-locations
Heading:  Leaflet Map Locations
```

Add the same Leaflet CSS and JavaScript imports used in `leaflet-js-quick-guide.xhtml`.

Inside `maincontent`, replace the generated form with:

```xhtml
<p:dataTable id="locations"
             value="#{mapLocationView.locations}"
             var="currentItem">

    <p:column headerText="Store Name">
        <h:outputText value="#{currentItem.storeName}"/>
    </p:column>

    <p:column headerText="Store Location">

        <div id="map#{currentItem.id}"
             style="height: 240px; width: 320px"></div>

        <h:outputScript>
            var map = L.map('map#{currentItem.id}')
                .setView([
                    #{currentItem.latitude},
                    #{currentItem.longitude}
                ], 13);

            L.tileLayer(
                'https://tile.openstreetmap.org/{z}/{x}/{y}.png',
                {
                    maxZoom: 19,
                    attribution: '&#169; OpenStreetMap'
                }
            ).addTo(map);

            var marker = L.marker([
                #{currentItem.latitude},
                #{currentItem.longitude}
            ]).addTo(map);
        </h:outputScript>

    </p:column>

</p:dataTable>
```

**What it does:** Displays a PrimeFaces DataTable with a separate Leaflet map and marker for each location.

---

### Main Week 7 Files

```text
dmit2015-1261-jakarta-data-hr-demo-main/
│
└── src/main/
    ├── java/dmit2015/
    │   ├── entity/
    │   │   └── (JPA entities generated from Oracle tables)
    │   ├── model/
    │   │   └── MapLocation.java
    │   └── view/
    │       └── MapLocationView.java
    │
    └── webapp/
        ├── leaflet-js-quick-guide.xhtml
        ├── leaflet-map-locations.xhtml
        └── WEB-INF/
            └── faces-templates/
                └── layout.xhtml
```

**Main ideas:**

- **Lesson 17:** Connect to Oracle and generate JPA entities using a database-first approach.
- **Lesson 18:** Integrate Leaflet.js with Jakarta Faces and display multiple map locations using a backing bean and PrimeFaces DataTable.