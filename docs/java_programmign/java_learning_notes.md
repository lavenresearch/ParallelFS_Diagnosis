# Java Learning Notes
This is notes of the oracle Java document
## Java environment
## Java programming language
### Concept
#### Class
##### definition

    class ClassName{
        int variable = 0;
        void method_name(int parameter){
            pass;
        }
    }

##### usage

    instance = new ClassName();
    instance.method_name(x);

##### inheritance
This gives "SubclassName" all the same fields and methods as "ClassName", yet allows its code to focus exclusively on the features that make it unique.

    class SubclassName extends class{
        int variable2 = 1;
        void method_name2(int parameter){
            pass;
        }
    }

#### Interface
In its most common form, an interface is a group of related methods with empty bodies.

Implementing an interface allows a class to become more formal about the behavior it promises to provide. Interfaces form a contract between the class and the outside world, and this contract is enforced at build time by the compiler. If your class claims to implement an interface, all methods defined by that interface must appear in its source code before the class will successfully compile.

    interface InterfaceName{
        void method(int parameter);
    }
    class ClassName implements InterfaceName{
        int variable = x;
        void method(int parameter){
            pass;
        }
    }

#### Package
A package is a namespace that organizes a set of related classes and interfaces.

The Java platform provides an enormous class library (a set of packages) suitable for use in your own applications. This library is known as the "Application Programming Interface", or "API" for short.

[Java Platform API Specification](http://docs.oracle.com/javase/8/docs/api/index.html) contains the complete listing for all packages, interfaces, classes, fields, and methods supplied by the Java SE platform.

### Classes and Objects
#### Declaring Classes
In general, class declarations can include these components, in order:

1. Modifiers such as public, private, and a number of others.
2. The class name, with the initial letter capitalized by convention.
3. The name of the class's parent (superclass), if any, preceded by the keyword extends. A class can only extend (subclass) one parent.
4. A comma-separated list of interfaces implemented by the class, if any, preceded by the keyword implements. A class can implement more than one interface.
5. The class body, surrounded by braces, {}.

#### Declaring Member Variables

There are several kinds of variables:

1. Member variables in a class -- these are called fields.
2. Variables in a method or block of code -- these are called local variables.
3. Variables in method declarations -- these are called parameters.

##### Access Modifiers

1. public modifier -- the field is accessible from all classes.
2. private modifier -- the field is accessible only within its own class

In this lesson, be aware that the same naming rules and conventions are used for method and class names, except that

1. the first letter of a class name should be capitalized, and
2. the first (or only) word in a method name should be a verb.

#### Defining Methods

More generally, method declarations have six components, in order:

- Modifiers—such as public, private, and others you will learn about later.
- The return type -- the data type of the value returned by the method, or void if the method does not return a value.
- The method name -- the rules for field names apply to method names as well, but the convention is a little different.
- The parameter list in parenthesis -- a comma-delimited list of input parameters, preceded by their data types, enclosed by parentheses, (). If there are no parameters, you must use empty parentheses.
- An exception list -- to be discussed later.
- The method body, enclosed between braces -- the method's code, including the declaration of local variables, goes here.

Definition: Two of the components of a method declaration comprise the method signature -- the method's name and the parameter types.

##### Method Name

Although a method name can be any legal identifier, code conventions restrict method names. By convention, method names should be a verb in lowercase or a multi-word name that begins with a verb in lowercase, followed by adjectives, nouns, etc. In multi-word names, the first letter of each of the second and following words should be capitalized. Here are some examples:

    run
    runFast
    getBackground
    getFinalData
    compareTo
    setX
    isEmpty

Typically, a method has a unique name within its class. However, a method might have the same name as other methods due to method overloading.

##### Overloading Methods

The Java programming language supports overloading methods, and Java can distinguish between methods with different method signatures. This means that methods within a class can have the same name if they have different parameter lists (there are some qualifications to this that will be discussed in the lesson titled "Interfaces and Inheritance").

Overloaded methods are differentiated by the number and the type of the arguments passed into the method. In the code sample, draw(String s) and draw(int i) are distinct and unique methods because they require different argument types.

You cannot declare more than one method with the same name and the same number and type of arguments, because the compiler cannot tell them apart.

The compiler does not consider return type when differentiating methods, so you cannot declare two methods with the same signature even if they have a different return type.

Note: Overloaded methods should be used sparingly, as they can make code much less readable.

#### Providing Constructors for Your Classes
Constructor declarations look like method declarations, except that they use the name of the class and have no return type.

One class can have multiple constructor with different parameters list.

##### Class without Constructor

You don't have to provide any constructors for your class, but you must be careful when doing this. The compiler automatically provides a no-argument, default constructor for any class without constructors. This default constructor will call the no-argument constructor of the superclass. In this situation, the compiler will complain if the superclass doesn't have a no-argument constructor so you must verify that it does. If your class has no explicit superclass, then it has an implicit superclass of Object, which does have a no-argument constructor.

##### Access constructor from other class

You can use access modifiers in a constructor's declaration to control which other classes can call the constructor.

Note: If another class cannot call a MyClass constructor, it cannot directly create MyClass objects.

#### Passing Information to a Method or a Constructor

##### Parameter Types

You can use any data type for a parameter of a method or a constructor. This includes primitive data types, such as doubles, floats, and integers, as you saw in the computePayment method, and reference data types, such as objects and arrays.

##### Arbitrary Number of Arguments

You can use a construct called varargs to pass an arbitrary number of values to a method.To use varargs, you follow the type of the last parameter by an ellipsis (three dots, ...), then a space, and the parameter name. The method can then be called with any number of that parameter, including none.

    public Polygon polygonFrom(Point... corners) {
        int numberOfSides = corners.length;
        double squareOfSide1, lengthOfSide1;
        squareOfSide1 = (corners[1].x - corners[0].x)
                         * (corners[1].x - corners[0].x) 
                         + (corners[1].y - corners[0].y)
                         * (corners[1].y - corners[0].y);
        lengthOfSide1 = Math.sqrt(squareOfSide1);
        // more method body code follows that creates and returns a 
        // polygon connecting the Points
    }

##### Passing Reference Data Type Arguments

Reference data type parameters, such as objects, are also passed into methods by value. This means that when the method returns, the passed-in reference still references the same object as before. However, the values of the object's fields can be changed in the method, if they have the proper access level.

For example, consider a method in an arbitrary class that moves Circle objects:

    public void moveCircle(Circle circle, int deltaX, int deltaY) {
        // code to move origin of circle to x+deltaX, y+deltaY
        circle.setX(circle.getX() + deltaX);
        circle.setY(circle.getY() + deltaY);
        // code to assign a new reference to circle
        circle = new Circle(0, 0);
    }

Let the method be invoked with these arguments:

    moveCircle(myCircle, 23, 56)

Inside the method, circle initially refers to myCircle. The method changes the x and y coordinates of the object that circle references (i.e., myCircle) by 23 and 56, respectively. These changes will persist when the method returns. Then circle is assigned a reference to a new Circle object with x = y = 0. This reassignment has no permanence, however, because the reference was passed in by value and cannot change. __Within the method, the object pointed to by circle has changed, but, when the method returns, myCircle still references the same Circle object as before the method was called.__

#### Returning a Value from a Method

__if a method return a object, does this object still exists in memory, will it be released with the method memory area.__

When a method uses a class name as its return type, such as whosFastest does, the class of the type of the returned object must be either a subclass of, or the exact class of, the return type. Suppose that you have a class hierarchy in which ImaginaryNumber is a subclass of java.lang.Number, which is in turn a subclass of Object.

Now suppose that you have a method declared to return a Number:

    public Number returnANumber() {
        ...
    }

The returnANumber method can return an ImaginaryNumber but not an Object. ImaginaryNumber is a Number because it's a subclass of Number. However, an Object is not necessarily a Number — it could be a String or another type.

You can override a method and define it to return a subclass of the original method, like this:

    public ImaginaryNumber returnANumber() {
        ...
    }

This technique, called covariant return type, means that the return type is allowed to vary in the same direction as the subclass.

Note: You also can use interface names as return types. In this case, the object returned must implement the specified interface.

#### Using the _this_ Keyword

Within an instance method or a constructor, _this_ is a reference to the current object -- the object whose method or constructor is being called. You can refer to any member of the _current object_ from within an instance method or a constructor by using _this_.

The most common reason for using the this keyword is because a field is shadowed by a method or constructor parameter.

For example, the Point class was written like this

    public class Point {
        public int x = 0;
        public int y = 0;
            
        //constructor
        public Point(int a, int b) {
            x = a;
            y = b;
        }
    }

but it could have been written like this:

    public class Point {
        public int x = 0;
        public int y = 0;
            
        //constructor
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
Each argument to the constructor shadows one of the object's fields — inside the constructor x is a local copy of the constructor's first argument. To refer to the Point field x, the constructor must use this.x.

Using this with a Constructor

From within a constructor, you can also use the this keyword to call another constructor in the same class. Doing so is called an explicit constructor invocation. Here's another Rectangle class, with a different implementation from the one in the Objects section.

    public class Rectangle {
        private int x, y;
        private int width, height;
            
        public Rectangle() {
            this(0, 0, 1, 1);
        }
        public Rectangle(int width, int height) {
            this(0, 0, width, height);
        }
        public Rectangle(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
        ...
    }

This class contains a set of constructors. Each constructor initializes some or all of the rectangle's member variables. The constructors provide a default value for any member variable whose initial value is not provided by an argument. For example, the no-argument constructor creates a 1x1 Rectangle at coordinates 0,0. The two-argument constructor calls the four-argument constructor, passing in the width and height but always using the 0,0 coordinates. As before, the compiler determines which constructor to call, based on the number and the type of arguments.

__If present, the invocation of another constructor must be the first line in the constructor.__

#### Controlling Access to Members of a Class

Access level modifiers determine whether other classes can use a particular field or invoke a particular method. There are two levels of access control:

1. At the top level—public, or package-private (no explicit modifier).
2. At the member level—public, private, protected, or package-private (no explicit modifier).

A class may be declared with the modifier public, in which case that class is visible to all classes everywhere. If a class has no modifier (the default, also known as package-private), it is visible only within its own package (packages are named groups of related classes — you will learn about them in a later lesson.)

At the member level, you can also use the public modifier or no modifier (package-private) just as with top-level classes, and with the same meaning. For members, there are two additional access modifiers: private and protected. The private modifier specifies that the member can only be accessed in its own class. The protected modifier specifies that the member can only be accessed within its own package (as with package-private) and, in addition, by a subclass of its class in another package.

The following table shows the access to members permitted by each modifier.

Access Levels
Modifier    Class   Package Subclass    World
public  Y   Y   Y   Y
protected   Y   Y   Y   N
no modifier Y   Y   N   N
private Y   N   N   N

##### Tips on Choosing an Access Level

1. Use the most restrictive access level that makes sense for a particular member. Use private unless you have a good reason not to.
2. Avoid public fields except for constants. (Many of the examples in the tutorial use public fields. This may help to illustrate some points concisely, but is not recommended for production code.) Public fields tend to link you to a particular implementation and limit your flexibility in changing your code.

#### Class Members

In this section, we discuss the use of the _static_ keyword to create fields and methods that belong to the class, rather than to an instance of the class.

Sometimes, you want to have variables that are common to all objects. This is accomplished with the static modifier. Fields that have the static modifier in their declaration are called static fields or class variables. They are associated with the class, rather than with any object. Every instance of the class shares a class variable, which is in one fixed location in memory. Any object can change the value of a class variable, but class variables can also be manipulated without creating an instance of the class.

Class variables are referenced by the class name itself, as in

    Bicycle.numberOfBicycles

Note: You can also refer to static fields with an object reference like

    myBike.numberOfBicycles

but this is discouraged because it does not make it clear that they are class variables.

The Java programming language supports static methods as well as static variables. Static methods, which have the static modifier in their declarations, should be invoked with the class name, without the need for creating an instance of the class, as in

    ClassName.methodName(args)

A common use for static methods is to access static fields. For example, we could add a static method to the Bicycle class to access the numberOfBicycles static field:

    public static int getNumberOfBicycles() {
        return numberOfBicycles;
    }

Class methods cannot access instance variables or instance methods directly -- they must use an object reference. Also, class methods cannot use the _this_ keyword as there is no instance for _this_ to refer to.

The _static_ modifier, in combination with the _final_ modifier, is also used to define constants. The _final_ modifier indicates that the value of this field cannot change.

    static final double PI = 3.141592653589793;

Constants defined in this way cannot be reassigned, and it is a compile-time error if your program tries to do so. By convention, the names of constant values are spelled in uppercase letters. If the name is composed of more than one word, the words are separated by an underscore (_).

Note: If a primitive type or a string is defined as a constant and the value is known at compile time, the compiler replaces the constant name everywhere in the code with its value. This is called a compile-time constant. If the value of the constant in the outside world changes (for example, if it is legislated that pi actually should be 3.975), you will need to recompile any classes that use this constant to get the current value.

#### Initializing Fields
##### Initializing Class variables

 Instance variables can be initialized in constructors, where error handling or other logic can be used. To provide the same capability for class variables, the Java programming language includes static initialization blocks.

 A static initialization block is a normal block of code enclosed in braces, { }, and preceded by the static keyword. Here is an example:

    static {
        // whatever code is needed for initialization goes here
    }

A class can have any number of static initialization blocks, and they can appear anywhere in the class body. The runtime system guarantees that static initialization blocks are called in the order that they appear in the source code.

There is an alternative to static blocks — you can write a private static method:

    class Whatever {
        public static varType myVar = initializeClassVariable();
        private static varType initializeClassVariable() {
            // initialization code goes here
        }
    }

The advantage of private static methods is that they can be reused later if you need to reinitialize the class variable.

##### Initializing Instance Members

 There are two alternatives to using a constructor to initialize instance variables: initializer blocks and final methods.

Initializer blocks for instance variables look just like static initializer blocks, but without the static keyword:

    {
        // whatever code is needed for initialization goes here
    }

The Java compiler copies initializer blocks into every constructor. Therefore, this approach can be used to share a block of code between multiple constructors.

A final method cannot be overridden in a subclass. This is discussed in the lesson on interfaces and inheritance. Here is an example of using a final method for initializing an instance variable:

    class Whatever {
        private varType myVar = initializeInstanceVariable();
        protected final varType initializeInstanceVariable() {
            // initialization code goes here
        }
    }

__This is especially useful if subclasses might want to reuse the initialization method. The method is final because calling non-final methods during instance initialization can cause problems.__

### [Nested Classes](http://docs.oracle.com/javase/tutorial/java/javaOO/nested.html)

to read

### Packages

Definition: A package is a grouping of related types providing access protection and name space management. Note that types refers to classes, interfaces, enumerations, and annotation types. Enumerations and annotation types are special kinds of classes and interfaces, respectively, so types are often referred to in this lesson simply as classes and interfaces.

You should bundle related classes and the interface in a package for several reasons, including the following:

- You and other programmers can easily determine that these types are related.
- You and other programmers know where to find types that can provide graphics-related functions.
- The names of your types won't conflict with the type names in other packages because the package creates a new namespace.
- You can allow types within the package to have unrestricted access to one another yet still restrict access for types outside the package.

#### Creating a Package
To create a package, you choose a name for the package (naming conventions are discussed in the next section) and put a package statement with that name at the top of every source file that contains the types (classes, interfaces, enumerations, and annotation types) that you want to include in the package.

The package statement (for example, package graphics;) must be the first line in the source file. There can be only one package statement in each source file, and it applies to all types in the file.

Note: If you put multiple types in a single source file, only one can be public, and it must have the same name as the source file. For example, you can define public class Circle in the file Circle.java, define public interface Draggable in the file Draggable.java, define public enum Day in the file Day.java, and so forth.

You can include non-public types in the same file as a public type (this is strongly discouraged, unless the non-public types are small and closely related to the public type), but only the public type will be accessible from outside of the package. All the top-level, non-public types will be package private.
If you put the graphics interface and classes listed in the preceding section in a package called graphics, you would need six source files, like this:

    //in the Draggable.java file
    package graphics;
    public interface Draggable {
        . . .
    }

    //in the Graphic.java file
    package graphics;
    public abstract class Graphic {
        . . .
    }

    //in the Circle.java file
    package graphics;
    public class Circle extends Graphic
        implements Draggable {
        . . .
    }

    //in the Rectangle.java file
    package graphics;
    public class Rectangle extends Graphic
        implements Draggable {
        . . .
    }

    //in the Point.java file
    package graphics;
    public class Point extends Graphic
        implements Draggable {
        . . .
    }

    //in the Line.java file
    package graphics;
    public class Line extends Graphic
        implements Draggable {
        . . .
    }

If you do not use a package statement, your type ends up in an unnamed package. Generally speaking, an unnamed package is only for small or temporary applications or when you are just beginning the development process. Otherwise, classes and interfaces belong in named packages.

#### Naming a Package

The compiler allows both classes to have the same name if they are in different packages. The fully qualified name of each class includes the package name.

##### Naming Conventions

Package names are written in all lower case to avoid conflict with the names of classes or interfaces.

Companies use their reversed Internet domain name to begin their package names—for example, com.example.mypackage for a package named mypackage created by a programmer at example.com.

Name collisions that occur within a single company need to be handled by convention within that company, perhaps by including the region or the project name after the company name (for example, com.example.region.mypackage).

Packages in the Java language itself begin with java. or javax.

In some cases, the internet domain name may not be a valid package name. This can occur if the domain name contains a hyphen or other special character, if the package name begins with a digit or other character that is illegal to use as the beginning of a Java name, or if the package name contains a reserved Java keyword, such as "int". In this event, the suggested convention is to add an underscore("_").

#### Using Package Members

The types that comprise a package are known as the package members.

To use a public package member from outside its package, you must do one of the following:

- Refer to the member by its fully qualified name
- Import the package member
- Import the member's entire package

Each is appropriate for different situations, as explained in the sections that follow.

##### Referring to a Package Member by Its Qualified Name

like this:

    graphics.Rectangle myRect = new graphics.Rectangle();

##### Importing a Package Member

    import graphics.Rectangle;
    Rectangle myRectangle = new Rectangle();

##### Importing an Entire Package

    import graphics.*;
    Circle myCircle = new Circle();
    Rectangle myRectangle = new Rectangle();


    // does not work
    import graphics.A*;

 you could import Rectangle and its nested classes by using the following two statements.

    import graphics.Rectangle;
    import graphics.Rectangle.*;

Be aware that the second import statement will not import Rectangle.

For convenience, the Java compiler automatically imports two entire packages for each source file: (1) the java.lang package and (2) the current package (the package for the current file).

##### Apparent Hierarchies of Packages

At first, packages appear to be hierarchical, but they are not. For example, the Java API includes a java.awt package, a java.awt.color package, a java.awt.font package, and many others that begin with java.awt. However, the java.awt.color package, the java.awt.font package, and other java.awt.xxxx packages are not included in the java.awt package. The prefix java.awt (the Java Abstract Window Toolkit) is used for a number of related packages to make the relationship evident, but not to show inclusion.

Importing java.awt.* imports all of the types in the java.awt package, but it does not import java.awt.color, java.awt.font, or any other java.awt.xxxx packages. If you plan to use the classes and other types in java.awt.color as well as those in java.awt, you must import both packages with all their files:

import java.awt.*;
import java.awt.color.*;
Name Ambiguities

If a member in one package shares its name with a member in another package and both packages are imported, you must refer to each member by its qualified name. For example, the graphics package defined a class named Rectangle. The java.awt package also contains a Rectangle class. If both graphics and java.awt have been imported, the following is ambiguous.

Rectangle rect;
In such a situation, you have to use the member's fully qualified name to indicate exactly which Rectangle class you want. For example,

graphics.Rectangle rect;
##### The Static Import Statement

There are situations where you need frequent access to static final fields (constants) and static methods from one or two classes. Prefixing the name of these classes over and over can result in cluttered code. The static import statement gives you a way to import the constants and static methods that you want to use so that you do not need to prefix the name of their class.

The java.lang.Math class defines the PI constant and many static methods, including methods for calculating sines, cosines, tangents, square roots, maxima, minima, exponents, and many more. For example,

public static final double PI 
    = 3.141592653589793;
public static double cos(double a)
{
    ...
}
Ordinarily, to use these objects from another class, you prefix the class name, as follows.

double r = Math.cos(Math.PI * theta);
You can use the static import statement to import the static members of java.lang.Math so that you don't need to prefix the class name, Math. The static members of Math can be imported either individually:

import static java.lang.Math.PI;
or as a group:

import static java.lang.Math.*;
Once they have been imported, the static members can be used without qualification. For example, the previous code snippet would become:

double r = cos(PI * theta);
Obviously, you can write your own classes that contain constants and static methods that you use frequently, and then use the static import statement. For example,

import static mypackage.MyConstants.*;
Note: Use static import very sparingly. Overusing static import can result in code that is difficult to read and maintain, because readers of the code won't know which class defines a particular static object. Used properly, static import makes code more readable by removing class name repetition.

#### Managing Source and Class Files
Put the source code for a class, interface, enumeration, or annotation type in a text file whose name is the simple name of the type and whose extension is .java. For example:

    //in the Rectangle.java file 
    package graphics;
    public class Rectangle {
       ... 
    }

Then, put the source file in a directory whose name reflects the name of the package to which the type belongs:

    .....\graphics\Rectangle.java

- class name -- graphics.Rectangle
- pathname to file -- graphics\Rectangle.java

When you compile a source file, the compiler creates a different output file for each type defined in it. The base name of the output file is the name of the type, and its extension is .class. For example, if the source file is like this

    //in the Rectangle.java file
    package com.example.graphics;
    public class Rectangle {
          . . . 
    }

    class Helper{
          . . . 
    }

then the compiled files will be located at:

    .\com\example\graphics\Rectangle.class
    .\com\example\graphics\Helper.class

##### CLASSPATH( where does jvm look for packages' .class file)
The full path to the classes directory, (path_two)\classes, is called the class path, and is set with the CLASSPATH system variable. Both the compiler and the JVM construct the path to your .class files __by adding the package name to the class path__. For example, if

    (path_two)\classes

is your class path, and the package name is

    com.example.graphics

then the compiler and JVM look for .class files in

    (path_two)\classes\com\example\graphics

A class path may include several paths, separated by a semicolon (Windows) or colon (UNIX). By default, the compiler and the JVM search the current directory and the JAR file containing the Java platform classes so that these directories are automatically in your class path.

## Java APIs
### Array

In java.util.Arrays class:

- Copy an array to a new places.
- Searching an array for a specific value to get the index at which it is placed (the binarySearch() method).
- Comparing two arrays to determine if they are equal or not (the equals() method).
- Filling an array to place a specific value at each index (the fill() method).
- Sorting an array into ascending order. This can be done either sequentially, using the sort() method, or concurrently, using the parallelSort() method introduced in Java SE 8. Parallel sorting of large arrays on multiprocessor systems is faster than sequential array sorting.

### Strings

    String a = "dddddd"
    String b = "ffffff"
    int len = a.length()
    a.concat(b) // concatenating a and b, returns "ddddddffffff"
    c = a + b // c = "ddddddffffff"
    String fs;
    fs = String.format("The value of the float " +
                       "variable is %f, while " +
                       "the value of the " + 
                       "integer variable is %d, " +
                       " and the string is %s",
                       floatVar, intVar, stringVar);
    System.out.println(fs);
    // convert string to number
    float a = Float.parseFloat("29.124");
    float b = Float.parseFloat("4589.154");
    // convert number to string
    int i=908;
    String s1 = "" + i
    String s2 = String.valueOf(i);
    String s3 = Integer.toString(i); 

## Collection
### useful information

1. [traverse a collection for Map](traversehttp://www.cnblogs.com/fczjuever/archive/2013/04/07/3005997.html)

### basic concepts

A collections framework is a unified architecture for representing and manipulating collections. All collections frameworks contain the following:

- Interfaces: These are abstract data types that represent collections. Interfaces allow collections to be manipulated independently of the details of their representation. In object-oriented languages, interfaces generally form a hierarchy.
- Implementations: These are the concrete implementations of the collection interfaces. In essence, they are reusable data structures.
- Algorithms: These are the methods that perform useful computations, such as searching and sorting, on objects that implement collection interfaces. The algorithms are said to be polymorphic: that is, the same method can be used on many different implementations of the appropriate collection interface. In essence, algorithms are reusable functionality.

The core collection interfaces encapsulate different types of collections. which are set, list, queue, deque and map. A Set is a special kind of Collection, a SortedSet is a special kind of Set, and so Forth. a Map is not a true Collection.

The following list describes the core collection interfaces:

Collection — the root of the collection hierarchy. A collection represents a group of objects known as its elements. The Collection interface is the least common denominator that all collections implement and is used to pass collections around and to manipulate them when maximum generality is desired. Some types of collections allow duplicate elements, and others do not. Some are ordered and others are unordered. The Java platform doesn't provide any direct implementations of this interface but provides implementations of more specific subinterfaces, such as Set and List. Also see The Collection Interface section.

Set — a collection that cannot contain duplicate elements. This interface models the mathematical set abstraction and is used to represent sets, such as the cards comprising a poker hand, the courses making up a student's schedule, or the processes running on a machine. See also The Set Interface section.

List[^ListVsArray] — an ordered collection (sometimes called a sequence). Lists can contain duplicate elements. The user of a List generally has precise control over where in the list each element is inserted and can access elements by their integer index (position). If you've used Vector, you're familiar with the general flavor of List. Also see The List Interface section.

Queue — a collection used to hold multiple elements prior to processing. Besides basic Collection operations, a Queue provides additional insertion, extraction, and inspection operations.
Queues typically, but do not necessarily, order elements in a FIFO (first-in, first-out) manner. Among the exceptions are priority queues, which order elements according to a supplied comparator or the elements' natural ordering. Whatever the ordering used, the head of the queue is the element that would be removed by a call to remove or poll. In a FIFO queue, all new elements are inserted at the tail of the queue. Other kinds of queues may use different placement rules. Every Queue implementation must specify its ordering properties. Also see The Queue Interface section.

Deque — a collection used to hold multiple elements prior to processing. Besides basic Collection operations, a Deque provides additional insertion, extraction, and inspection operations.
Deques can be used both as FIFO (first-in, first-out) and LIFO (last-in, first-out). In a deque all new elements can be inserted, retrieved and removed at both ends. Also see The Deque Interface section.

Map — an object that maps keys to values. A Map cannot contain duplicate keys; each key can map to at most one value. If you've used Hashtable, you're already familiar with the basics of Map. Also see The Map Interface section.
The last two core collection interfaces are merely sorted versions of Set and Map:

SortedSet — a Set that maintains its elements in ascending order. Several additional operations are provided to take advantage of the ordering. Sorted sets are used for naturally ordered sets, such as word lists and membership rolls. Also see The SortedSet Interface section.

SortedMap — a Map that maintains its mappings in ascending key order. This is the Map analog of SortedSet. Sorted maps are used for naturally ordered collections of key/value pairs, such as dictionaries and telephone directories. Also see The SortedMap Interface section.
To understand how the sorted interfaces maintain the order of their elements, see the Object Ordering section.

[^ListVsArray]: [list vs. array](http://pages.cs.wisc.edu/~paton/readings/Lists/index.html)
