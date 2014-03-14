SimpleFootie
============
This is a text-based football match engine simulation. This is not my first one. A previous attempt, "openfootie", 
created some 4 years ago is already pushed.

I am happy with openfootie as a first attempt and thing that "just works", but now starting again with some lessons learned:

- Start simple

I wanted to create a sufficiently complex match engine, complex enough for having a realistic element. Ok, I did that
but now I need to go back and start simple again. Trying to do a complex thing as a first attempt, you inevitably
mis-implement some abstractions and violate your initial assumptions. Then, you somehow lose the understanding of
how things work. Finally, you introduce redundant detail for the maximum level of realism you will achieve anyway.

- Integrate

A football match engine is not an island in itself. I wanted to create openfootie as a library which would be plugged
into a "host" application, whether it was a game or some other kind of simulation. This part I think failed miserably.
At the same time, I treated the match engine as agnostic to the domain in which would be applied. However, I don't
think now this was a good choice. I think the simulation must be built at least with a minimal layer of host 
functionality around it, for at least the inputs and outputs are affected by the domain and inevitably this will affect 
the mechanics of the engine itself.

Based on the above thoughts, I am starting a simple football (soccer, duh!) match engine simulation from the very basics 
(this version will implement just scores calculation) with the aim of raising the level of sophistication gradually.
There are a lot of approaches you could follow and of course there is eventual visualization. My aim is to create a 
unified open source reference for football (and other sports' possibly) match engines, where different levels of 
abstraction and approaches will be tried.

Currently, I am also working on an openfootie rewrite (an example of another approach), which strips away all the 
redundant details and fixes some deviations from the original assumptions. However, this is going to be a more 
slow-paced, experimental kind of sub-project.

So, this project is about simulating score calculations. Nothing fancy at first glance but good to build
a basis on. It also includes a Java web application interface, if you don't want to run it through the console (I assume 
you will use an IDE to run it anyway). No overhead of database configuration needed (as in other similar projects like 
openfootie), so you just set your server's path in the build.properties file (I used Apache TomEE as it's written in the 
file), deploy based on build.xml and hopefully it will work. There is one minor variation in features between the 
concole and the web app, and the console edition will be generally more complete.

