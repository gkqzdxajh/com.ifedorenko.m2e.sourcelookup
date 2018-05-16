# Setup

* Import sourcelookup demo webapp as existing maven project

# Default behaviour, breakpoint in workspace project

* Run sourcelookup/webapp as maven build with goal 'jetty:run'
** enable workspace resolution
** disable advanced source lookup in java debug preferences
* Open in web browser http://localhost:8080/

Expected: [a, b, Hello, world!]

* Set breakpoint at WebappServlet #36 
* Debug the webapp

Expected: "Source not found." editor

* Enable Dynamic Sources Lookup launch extension (it _is_ enabled by default)
* Debug the webapp

Expected: debugger properly positioned at WebappServlet sources
Expected: Debug Shell view provides codeassist for 'my...' mylist local variable, shows mylist value
Expected: Variables view, open actual type of mylist opens sources from webapp/ project
Expected: Variables view, open declared type of lib opens sources from lib/ project

# Conditional breakpoint in workspace projects

* Edit condition of the breakpoint at WebappServlet #36, set lib != null

Expected: codeassist works in breakpoint condition editor field
Expected: debugger stops at the breakpoint

# Indexer-based external sources lookup (index download is disabled by default in m2e 1.5+)

* Select HttpServlet.service #735 stack frame

Expected: jetty javax.servel-3.0.0 sources jar is downloaded and debugger shows source file
Expected: Open Pom on the stack frame does not work

* Put breakpoint at HttpServlet #732

(Kinda) Expected: breakpoint does not appear in lefthand ruler due to jdt bug/limitation, the breakpoint view is fine
Expected: debugger stops at the breakpoint
Expected: Debug Shell view does not provide codeassist, does not show variable values
Expected: Variable view, open actual, declared types of resp opens wrong Response.java, wrong HttpServletResponse.java

* Enable condition of the breakpoint at HttpServlet #732, resp != null

Expected: codeassist does not work
Expected: debugger stops with "unable to compile conditional breakpoint" error message 

* Remove breakpoint at HttpServlet #732 (need to use Breakpoints view)

# pom-based external sources lookup

* Select ServletHolder.handle #598 stack frame

Expected: ServletHolder.java is displayed
Expected: Open Pom on the stack frame opens jetty-servlet 8.1.4.v20120524 pom.xml

# Sourcelookup properties

Expected: dialog shows expected values, Copy and Refresh work (refresh can be tested by closing source viewer)
Problem: 2017-10-19 performs remote calls on ui thread


# Binary project

* Import binary project on HttpServlet.service #735 stack frame

Expected: org.eclipse.jetty.orbit_javax.servlet_3.0.0.v201112011016 project is created in workspace
Expected: debugger automatically refreshes to show HttpServlet.java from workspace binary project  
Problem: 2017-10-19 race between WorkspaceProjectSourceContainers#updateProjects and ImportBinaryProjectHandler#importBinaryProjects
         ideally, should refresh frame after WorkspaceProjectSourceContainers#updateProjects
Expected: Display view provides codeassist for 'me...' method variable, shows the variable value

Expected: Variables view, show declared type of Response shows sources from binary project
Expected (see problem below): show actual type fails with "Source not found" pop-up
Problem: as of 2012-12-22, show actual/declared type only work when target type comes from a workspace project.
         This seems like unnecessary jdt ui limitation around OpenTypeAction, which requires IJavaElement and
         does not allow sources from external jars or source directories.

* Import binary project on ServletHolder.handle #598 stack frame

Expected: org.eclipse.jetty:jetty-servlet:8.1.4.v20120524 project is created in workspace
Expected: debugger automatically refreshes to show ServletHolder.java from workspace project
Problem: 2018-05-15 didn't refresh, same race as above? 
Expected: Display view provides codeassist for 'se...' sevlet variable, shows the variable value

# Thread context sources lookup

* Run "mvn clean install" on lib module, close lib workspace project

Expected: webapp project switches workspace dependency to locally installed lib-0.0.1-SNAPSHOT.jar
Problem: 2017-10-19 webapp didn't automatically update dependencies

* Set breakpoint on Lib #17, then debug jetty:run, etc

Expected: Lib.java from webapp classpath dependency jar is displayed

* Import lib-0.0.1-SNAPSHOT as binary project (from stack frame right-click)
* Restart webapp
Expected: Lib.java from binary project is displayed

* Delete lib-0.0.1-SNAPSHOT workspace project
Expected: source lookup switches back to webapp classpath dependency

* Refresh source viewer from sourcelookup properties dialog or by swithcing stack frames
Expected: source lookup uses webapp classpath dependency

