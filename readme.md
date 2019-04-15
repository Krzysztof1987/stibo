# Demo Project

This is a demo project, which shows some of the data model inteactions we do in the Tagglo/PDS project.

The project objective is to generate a tabular report in CSV format from our data standard specification.

Please fork the project to you own bitbucket, github, or other git hosting account, and give us access to the repository holding your solution.

## Data Standard

We have a data standard, which describes the context for our products.

The data standard has a list of *categories*, which disjointly categorizes the products.
The categories are linked together in a hierarchy, where a category points to it's parent.
E.g., for a *Clothing* category, we could have sub-categories *T-Shirts* and *Jeans*.
There is always a single root, where the parent is not set.

The data standard also has a set of *attributes*, which describes the data on the products.
The attribute has a type, which specifies the type of the value. E.g., an attribute could be a *Description* of type
*string* and a *Price* of type *decimal*. The attributes are linked into the categories using *attribute links*.
The available attributes for a category, is the linked in attributes for all categories from the given category to
the root. E.g., attributes defined in the *Clothing* category are also available for products in the *T-Shirts* and
*Jeans* categories.

Some attributes also link in other attributes, which means that this attribute is a composite definition. An attribute
in a composite definition can also be a composite.

The data standard also defines a set of *attribute groups*, which is an ability to scope the attributes, where
the same attribute can be linked in to multiple attribute groups. E.g., you could have a group with all assets,
and other groups also including some assets. It is a tool for the use to filter the attributes.

Some data standards have close to 10,000 categories and 10,000 attributes. The available attributes in a category
can exceed 500.

There is a trivial test data standard in the [datastandard.json](src/test/resources/datastandard.json) test resource.
The object model for the data standard is defined in the [model](src/main/java/com/stibo/demo/report/model) package.

## Deliveries

The assignment is to implement the *report* method in the [Reportservice](src/main/java/com/stibo/demo/report/service/ReportService.java)
service. The method takes a data standard object and a category id as argument, and should return a list of list of strings.
The outer list is a list of tabular rows, and the inner lists are a list of cells in a row. The controller
[endpoint](src/main/java/com/stibo/demo/report/controller/ReportController.java) makes a naive conversion of this to CSV.

There is a [test](src/test/java/com/stibo/demo/report/service/ReportServiceTest.java) setup, which can be used to exercise
the service call.

The deliveries are split into 3 levels. You do not have to support all levels at the same time, it is just a way of splitting
the assignment in parts, to set goals during the development. The added content from the previous level is in __bold__.

Notice, that the method should return the content of the tables shown below. The above mentioned endpoint converts it to csv.

### Level 1

Produce a report like this on the test data standard:

    +---------------+-----------------+-----------------------------+-----------+
    | Category Name | Attribute Name  | Description                 | Type      |
    +---------------+-----------------+-----------------------------+-----------+
    | Root          | String Value    |                             | string    |
    +---------------+-----------------+-----------------------------+-----------+
    | Leaf          | Composite Value | Composite Value Description | composite |
    +---------------+-----------------+-----------------------------+-----------+

The fields are:

- **Category Name:** the name of the category, the attribute is linked in to.
- **Attribute Name:** the name of the attribute.
- **Description:** the description of the attribute.
- **Type:** the type id of the attribute.

### Level 2

Produce a report like this on the test data standard:

    +---------------+-----------------+-----------------------------+-------------+---------+
    | Category Name | Attribute Name  | Description                 | Type        | Groups  |
    +---------------+-----------------+-----------------------------+-------------+---------+
    | Root          | String Value*   |                             | string      | All     |
    +---------------+-----------------+-----------------------------+-------------+---------+
    | Leaf          | Composite Value | Composite Value Description | composite[] | All     |
    |               |                 |                             |             | Complex |
    +---------------+-----------------+-----------------------------+-------------+---------+

The fields are:

- **Category Name:** the name of the category, the attribute is linked in to.
- **Attribute Name:** the name of the attribute, __with an asterisk (*) appended, if the link to the attribute has the *optional* field set to false__.
- **Description:** the description of the attribute.
- **Type:** the type id of the attribute, __with brackets ([]) appended, if the *multiValue* field is set to true__.
- **Groups:** __the names of the attribute groups listed in the *groudIds* field, separated by new lines__.

### Level 3

Produce a report like this on the test data standard:

    +---------------+-----------------+-----------------------------+--------------------------+---------+
    | Category Name | Attribute Name  | Description                 | Type                     | Groups  |
    +---------------+-----------------+-----------------------------+--------------------------+---------+
    | Root          | String Value*   |                             | string                   | All     |
    +---------------+-----------------+-----------------------------+--------------------------+---------+
    | Leaf          | Composite Value | Composite Value Description | composite{               | All     |
    |               |                 |                             |   Nested Value*: integer | Complex |
    |               |                 |                             | }[]                      |         |
    +---------------+-----------------+-----------------------------+--------------------------+---------+

The fields are:

- **Category Name:** the name of the category, the attribute is linked in to.
- **Attribute Name:** the name of the attribute, with an asterisk (*) appended, if the link to the attribute has the *optional* field set to false.
- **Description:** the description of the attribute.
- **Type:** the type id of the attribute, with brackets ([]) appended, if the the *multiValue* field is set to true.
__If the *attributeLinks* field is set, then the links are _recursively expanded in braces ({}) as: two spaces, attribute name with optional marker,
colon (:), and expanded type, separated by new lines.__.
- **Groups:** the name of the attribute groups listed in the *groudIds* field, separated by new lines.

# Real Data

If the application is started, it can be tested on real data as

    curl -s -u key:secret http://localhost:8080/report/acme/T_SHIRTS

The expected content of this call is exported to the [t-shirts.csv](doc/t-shirts.csv) file, and imported into the corresponding [T-Shirts.xslx](doc/T-Shirts.xslx) file.