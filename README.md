<h1>Women-Safety</h1>
<p>This application was created as a part of GirlScript India Summit 2020 Hackathon by Deep Sutariya, Aniket Kariya, Ridham Chitre and Shrusti Thakkar. </p>

<hr>

<h3> Problem Statement </h3>
<p>When there is no one to go to In times of danger or trouble, there is nowhere to seek help from, like the police or any force of authority that might help in the situation, how can this be addressed ?</p>

<h3> Solution  </h3>
<p>
 	<ul>
    	<li> The solution is to create an app that allows those who need help to ask for help from the nearby people in the times when there is no police or other forces available. </li>
    	<li> The app will broadcast the message to the users of the app within the radius of 1 KMs. The message will contain the location of who needs help.  </li>
		<li> It will also send the location to the pre-specified contacts via SMS. </li>
		<li> The app will also show nearby public places to go to which are generally safe in case if the victim might be new to city </li>
		<li> The victim can also specify a message, in there is something important to convey </li>
		<li> There can be a frontend dashboard for police/authorities so that they can keep track of these things </li>
		<li> In case there is no network, or GPS has been disabled, the app will keep track of last known location and send as soon as it goes online </li>
 	</ul>
</p>

<h3> Limitations </h3>
<p>
 	<ul>
		<li> Internet Connection and GPS </li>
		<li> People not coming to help even after receiving the message </li>
		<li> Battery saver - prevents app from running in background and tracking location </li>
 	</ul>
</p>

<h3> Note </h3>
<p> The project files does not contain manifest file for security reasons since it containas the API Keys. It might not run on your machine. You'll need google-services.json and Here Maps API Keys to be manually specified </p>
<p> It is prototype for the hackathon, made in 12 hours. It does not implement all the functioanlities mentioned above. </p>

<h3> Technical </h3>
<p> The application is created with the use of Google Auth sign in, Firebase, and Here Maps. <p>
<p> There are 4 activities. The first asks for use to sign in. Second is primary activity, which keeps track of user location. There is a broadcast receiver which updates the location on the server on a fixed interval. </p>
<p> The user will be notified if someone in the radius of 1 km needs help. same way, user can ask for help as well, which will notify the people in the radius of 1 Km and contacts <p>
<p> Those who is coming for help can see their and those who need help's location the map -- on the fourth activity. The location will be continuously updating from firebase realtime database. </p>
<p> There are two services, implementing notifications. One is for sending, one is for receving. A class UserInfo, which contains the user data -- name, email, latitude, longitude and last location udpate. An object of the same class will be used to update the information on the firebase, same information stored.
<h3> More references </h3>
<p>
	<li> <a href="https://drive.google.com/file/d/1m1HY6_qDdb4KmAToFZ6B8gifReLRXm8q/view?usp=sharing"> Presentation </a> </li>
	<li> <a href="https://www.youtube.com/watch?v=lIPchaRvDfU"> YouTube </a> </li>
</p>
