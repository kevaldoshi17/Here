## Inspiration
Thirty million small-business jobs are vulnerable—a higher share than at larger private-sector employers. A precipitous surge in unemployment continues to shake the US workforce in the wake of COVID-19. Total claims reached 30 million in the six weeks since March 14th. 

The challenge is especially acute for small businesses (those with 500 or fewer employees), which account for a disproportionate share of the vulnerable jobs. Before COVID-19, they provided nearly half of all US private-sector jobs, yet they account for 54 percent (30 million) of the jobs most vulnerable during COVID-19.

## What it does

### For the user
We feel that the local businesses having to compete with large chains is detrimental to their sustainability, especially in this environment. So with the help of a little AI, we created a chatbot type interface that suggests you some local places to try. Instead of always showing you the same set of places nearest to you, it monitors the traffic attracted at each of these locations and gives preference to those places that are not crowded so that it is easier for social distancing.  

### For the owner 
The owner has the option to register with us and if they have cctv cameras, they can stream the video through us. We monitor the video in real-time using neural networks and notify the owner if people are not social distancing. These businesses also get a HERE verified mark, which increases the trust of a user.

## How I built it
App:

Backend: The backend is completely hosted on Azure and uses It's various API's. The chatbot is built using LUIS from Azure's cognitive services. We find local businesses using Bing's Local search API. The neural net model for detecting people and checking if they are social distancing is again hosted on a VM on Azure. 

## Challenges I ran into

One of the biggest challenges was to train the NLP model to recognize cuisines as we could not find a pretrained model for that. Also understanding the intent of the user was difficult to train for. Different time zones also meant that we had a hard time scheduling when to work but we managed it in the end! 

## Accomplishments that I'm proud of


## What I learned

## What's next for HERE
