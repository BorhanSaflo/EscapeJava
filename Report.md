
# COMP 2800 Group Project Report

#### ***Authors:** Laila Albalkhi, Ryan Hermes, Tanzim Hossain, Yousef Kart, Borhan Saflo*
#### ***Date:** April 8th 2023*
<hr/>

## **1. Introduction**

### **1.1 Purpose of the project**
When we first set out to create a Java3D project as a group, we initially considered a variety of options. We explored designs for a racing game, , and even an implementation of Flappy Bird. We were looking for something that would prioritize user interaction, complete with a detailed and realistic interface. Ultimately, we decided that a virtual escape room would be the perfect opportuntity to meet our requirements. With that, Escape Java was born.

The purpose of our Escape Java project was threefold. First, we wanted to create a project to showcase our understanding of object-oriented software development in the scope of Java3D. Second, we wanted to demonstrate our mastery of the Scrum framework and the adoption of the Agile development methodology. Third, we wanted to provide an interactive and engaging experience for our audience.

Escape Java was a great opportunity for us to develop our software development skills and apply the things we learned in the labs and assignments throughout our course to a creative endeavour of our own. Escape Java was a direct application of our understanding of animated objects, digital sensory features, and virtual spaces. We'll go more in depth about our software development process in section 3 of this report. 

The purpose of this project was also to use the Agile methodology, particularly the Scrum framework, to complete our tasks efficiently and effectively. Our project demonstrates our ability to work collaboratively as a team, collectively contributing our individual skillset and expertise to accomplish a common goal. 

As proven by the voting results in the last week of class, our project was engaging and interactive. We chose to model our virtual space on the Java Lounge, a frequented room in Erie Hall, to incorporate an element of realism into our digital project. The audience was quick to recognize a familiar setting, increasing the level of engagement in the virtual world we created. 

### **1.2 Issues to be discussed and their significance**

The first step of modelling the Java Lounge posed a significant problem. Since we were using a real-life room as reference, we had very little margin for error and were required to pay a lot of attention to detail. We initially tried to scan the room as a whole using a mobile application to scan 3D objects. This presented a new issue; the scan we took morphed the objects of the room into one singular object, hindering our ability to interact with individual pieces and position them. We ultimately decided to model each object of the room separately and position them individually. While this approach took more time and almost 4 decimal points of transition coordinates, it resulted in a life-like representation of the Java Lounge.

Navigation around the room was also another issue. We faced a problem of defining the camera's path while simultaneously allowing the user to look around at their surroundings. Since one of our key priorities for our project was user engagement, it was crucial to emphasize the user's perspective. This is why we placed the viewing capabilities and mechanisms in the control of the user themselves. The multiple views are defined by their navigation around the room, being able to turn around, crouch, and even elevate to search the room from a bird's eye view.

We also faced some issues with using the Scrum methodology regularly and consistently. As a group of full-time undergraduate students with a number of other commitments, it was difficult to commit to the number of hours specified in our capacity every day. Our capacity realistically fluctuated, increasing some days and decreasing others. This created some issues in our burn down chart, which we will discuss in greater detail in section 2. Another issue we faced was equally sharing tasks, since Tanzim literally wouldn't let any of us do anything.

### **1.3 Project methods**

We used a number of techniques, processes, and toold to manage and execute Escape Java. In the context of our project, we primarily used agile development, version control, continuous integration and delivery, test-driven development, and pair programming.

Adopting the Agile development methodology was the crux of our project and defined the foundation of what we would eventually build. The essential implementation of the Agile methodology was breaking our project into smaller tasks. We used the Agile manifesto as reference, prioritizing early and continuous delivery of software, welcoming changing requirements, and delivering working software frequently. We used Scrum specifically to manage our project, helping us prioritize tasks, set goals, and iterate on our development process based on feedback from our team. 

As a team of 5 working together, version control was an imperative component of our work together. We used Git as our version control system to keep track of changes to our codebase. Git helped us maintain multiple versions of our code, collaborate on code changes, and quickly revert to previous versions if necessary. We worked on multiple branches to minimize conflicts and used detailed commit messages to document our iterative changes to the overall project.

Test-driven development was another key component of the development of Escape Java. When we identified a new use case or story to implement, we defined the test cases that would indicate a successful completion of that task and then developed the code to pass those tests. We translated the requirement of the feature into a unit or integration test, and then wrote and implemented the code to fulfill that requirement. Through test-driven development, we were able to innovate faster and reduce unnecessary implementations, by only creating enough code to pass the test.

We often met online to work on the project together, and in many situations we participated in pair-programming in smaller groups. One person would share the code they were implementing as the on-lookers provided feedback. We used this method during critical stages of the project to ensure that complex parts of the code were developed in a collaborative fashion. This utimately helped us reduce errors, improve code quality, and increase the amount of understanding we collectively had as a group.

### **1.4 Limitations and assumptions**

The main limitations of our project were those that Java3D presented. As a fairly outdated library, we didn't have an adequate amount of resources or documentation to support us through the development of our project. Additionally, there were a few restrictions when it came to importing objects from Blender. We faced some difficulty in directly attaching an object's texture file from Blender. This motivated the creation of our MTLFile parser, which took a material file as input and applied it to its respective 3D object.

In terms of creative endeavours, we were limited by the realistic virtualization of our project. We had to comply with the physical blueprint of the Java Lounge itself to maintain its referential integrity and representation. This meant that the puzzles we incorporated into the project were required to be those that complimented the physical space; we included colored boxes, clues on the TV screens, and interaction with the high chairs.

## **2. Project Management with Scrum**

### **2.1 Use cases as backlog items**

After the modelling of the room was complete, the first use cases were related to the user's navigation and interaction around the room. These use cases became backlog items for the game features. This included use cases like:
- Move: The user moves around the room, moving the camera as well.
- Crouch: The user crouches down, lowering the camera's view.
- Hover: The user hovers above their position, getting a bird's eye view.
- Inspect: The user clicks on an object to inspect it, zooming it into view.
- Collide: The user collides with an object, unable to move through it.

Then, we started to implement puzzles and clues around the room to increase the user's level of engagement and interaction with their surrounding virtual environment. The use cases transformed into tasks that implemented the logic of the puzzles. This included:
- Input code: The user inputs a numeric code into the computer.
- Turn chair: The user presses 'E' to turn the chair to face a certain direction.
- Rotate lock wheel: The user uses the mouse to scroll the lock to find the correct combination.

### **2.2 Creation of tasks**

At the start of each sprint, we dedicated a meeting to identifying and assigning the tasks of the upcoming week to everyone on our team. We made a list of the tasks we aimed to complete based on the above use cases and assigned them based on everyone's capacity for that week and their projected availability. Due to the intertwined nature of a lot of our features, each backlog item had a set of tasks associated with it. Typically, we aimed to assign specific items to someone that was skilled or interested in that task. 

To embody the Scrum methodology, our expectations and tasks iteratively and continuously changed as our project progressed. We realized that there were many items we ideally would have liked to complete, but needed to be cut due to the time constraints we had. Our main goal was to ensure that at any point in time of our project, the user would experience complete functionality and experience no bugs. The puzzles were added incrementally, one after the other. 

### **2.3 Planning and Carrying out the Sprint**

Our sprints were a week long, our first one starting the week after reading week. Our first sprint was heavily focused on the research and requirements gathering portion of the project and spread out evenly among all team members. Borhan, our Scrum master, continuously and regularly monitored the sprints to ensure we were on track, adjusting tasks and our capacities as needed. By default, our sprints were set for only weekdays. We recognized later that this was not representative of our work done, as a lot of us used our weekends to work on the project.

### **2.4 Team management**

To ensure regular communication, we created a Discord group chat to organize synchronous meetings and to hold asynchronous discussions. We met for 15-minutes every night to discuss our individual progress and make any team-wide decisions that would affect the entirety of the project. 

To manage our team effectively, we made sure to enforce clear roles and responsibilities. We worked together to share ideas and give each other feedback on the code we implemented. We also made sure to celebrate our successes to keep team morale high. This fell in line with the Scrum methodology, as it encourages a culture of continuous improvement. We were able to optimize our work and deliver a high-quality project by regularly evaluating our processes and identifying areas for improvement.

## **3. (Object-Oriented) Software Development**
### **3.1 Identification of classes**
- idk help

### **3.2 Software design with class diagram(s)**
- also help

### **3.3 Techniques of implementation**
- test-driven development
- abstratction
- encapsulation
- inheritance
- polymorphisom
- design patterns
- this is all bs

We used abstraction to simplify the design of our game and make it more manageable. This helped us create simple and reusable objects that could be easily integrated into the game, including the chairs and tables that were regularly repeated around the room. We also used polymorphism to create objects that could behave differently based on their context. This enabled us to create a more dynamic and flexible game with objects that could adapt to different situations, including the clues that the user was able to interact with.

### **3.4 Software testing and operation**

We used a combination of unit-testing, integration testing, and acceptance testing to ensure that our project was progressing on the right track and meeting the specified requirements. 

Using unit tests helped us catch errors early in the development process and ensure that the individual components of the game were functioning correctly. Our unit tests made sure that any added feature did not jeopardize the integrety of our other implemented features.

Integration testing was important to test how the various components worked together as a system, such as the game engine, the user interface, and the puzzle logic. Integration testing caught any errors that were missed by our unit tests.

We also implemented a variety of acceptance tests to validate the requirements of our end-users. We were able to gather feedback from our personal experiences with the game and adjust our implementation accordingly.

Throughout our testing process, the most important thing was to regularly monitor and log our progress. Any failed or fixed tests were documented and used as references when a new bug appeared. This was crucial in making sure that the performance of our game was monitored, giving us the opportunity to proactively identify and resolve issues and ensure that the game remained performant for users.

<hr/>

## **4. Discussions and Recommendations**
### **4.1 Discussions about the team projects**
- we're bestestest of friends now
### **4.2 Recommendations for better practices**
- starting earlier
- clear set roles

## **Appendix A: List of Use Cases and Tasks with Brief Descriptions**

- uhhh

## **Appendix B: Contribution Table**

| Backlog Item Title | Task Title | Borhan | Laila | Tanzim | Ryan | Yousef |
| ----- | ----- | ----- | ----- | ----- | ----- | ----- |
| Backlog_Item_Title1  | task_title11  | x  |
|   | task_title12  | x  |



## **Appendix C: Participation Table**
| Meeting Date | Duration | Borhan | Laila | Tanzim | Ryan | Yousef |
| ----- | ----- | ----- | ----- | ----- | ----- | ----- |
| date1 | t1 | x
| date2 | t2 | x
| **Total** | x | x

