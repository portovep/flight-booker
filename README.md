FlightBooker
============

Started (and ended) as an individual academic project to be developed during three months. As students, we were asked to design and implement a scalable distributed booking system working in a constrained environment which tries to simulate scenearios that can be found in delay tolerant networks (DTN). The system requirements involved seat allocation fairness, load-balancing, data replication and fault tolerance to warranty that the system can process booking requests issued from different locations and under different connectivity conditions.


Technologies
------------
One of the requirements was that we could not use anything more abstract than sockets. Therefore, my design was based in a custom communication protocol built on top of Java sockets. Amon other things, Java threads were used to implement concurrency in the server side and MySQL DBMs was used to implement data persistency.


Requirements
------------
The requirements were extracted from the project description document and the lecture notes. The task was to design & build a system which meets them as accurate as possible. The project description was the following:

> The scenario for this system is a (real!) remote encampment in northern Scandinavia. The
encampment can only be reached by helicopter, and is serviced by three private helicopter com-
panies providing access to the nearest town. Visitors and residents can book passage in or out
of the encampment through any helicopter company. The number of seats on a helicopter flight
is very limited and the companies are collectively responsible for satisfying demand, and must
load-share as necessary.

> You have to design, build, test and demonstrate a distributed booking system for the heli-
copter companies.

> The core idea of the project is to choose and implement an appropriate organization of the
system and suitable algorithms for ticket booking and cancellation. The system should not
impose arbitrary restrictions on the ability of potential clients to make or cancel bookings. It
must when a number of helicopters are in flight—in particular, it must not be confined to working
only when one helicopter is in transit.

> The prototype implementation should use only sockets for communication between the parts
of the system and should be written in Java.
Your system should be built in a way that conforms to good distributed system practice. Your
design may ignore security concerns. You may assume that nodes and disks do not fail but you
should allow for the possibility of communication failure and for individual node unavailability.
You need only provide a rudimentary user interface. In designing the system you should consider
that such a system might have to scale to support different deployments

Design
------
The system is divided in 4 components:

+  **DBS-Camp** - Allows the user to send request from the camp. The requests are forwarded to an helicopter.
+  **DBS-City** - Allows the user to send request from the city.
+  **DBS-Helicopter** - Simulates the helicopters that travel between location. It has a buffer where the booking request are stored.
+  **DBS-Server** - Coordinates the operation of all the other components by processing request, ensure fairness, load-balancing and data persistency.