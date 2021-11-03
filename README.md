<h2>Pre-requisites</h2>

You need to make sure the below programs work in your terminal

<ul>
  <li><b>javac</b></li>
  <li><b>jar</b></li>
  <li><b>java</b></li>
</ul>

<p>I have used jdk 11, but anything above jdk8 should work
<h2>Usage</h2>

<h3>To Compile</h3>

<ol>
  <li> <code>
<i>javac -cp "lib/*" -d "bin/"  src/PrefixFilter.java</i> </code>
</li>
  <li> <code>
<i>jar -cfm filter.jar manifest.txt bin/PrefixFilter.class </i></code>
 </li>
</ol>

<p> "filter.jar" can be replaced with any name to create jar with different name

<h3>To Run</h3>

<code>
<i>java -jar filter.jar [path/to/prefix.yaml] </i></code>


<b>Note:</b>
<ol>
  <li>prefixes.yaml is the default file the program will look for</li>
  <li>custom yaml file can be passed as command line argument to use instead</li>
  <li>Valid file format below</li>
 <ol>


<pre>
prefixes:
 - .1.3.6.1.6.3.1.1.5
 - .1.3.6.1.2.1.10.166.3
 - .1.3.6.1.4.1.9.9.117.2
 - .1.3.6.1.2.1.10.32.0.1
 - .1.3.6.1.2.1.14.16.2.2
 - .1.3.6.1.4.1.9.10.137.0.1
</pre>
