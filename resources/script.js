function countdown(){
    const stop = document.getElementById('stop');
    stop.style = 'display: none;';
    const counter = document.getElementById('counter');
    let countdown = 5;
    const interval = setInterval(() => {
        countdown--;
        if(countdown === 0){
            clearInterval(interval);
            stop.classList.add('rotate');
            stop.style = '';
            stop.addEventListener('click', () => document.body.classList.remove('rotate'))
        }
        counter.innerHTML = `Let's spin in ${countdown}...`;
    }, 1000);
}

function rainbow(buttonId = 'rainbow') {
    const button = document.getElementById(buttonId);
    button.innerHTML = "ARCOBALENO!";
    let angle = 0;
    setInterval(() => {
        angle = (angle + 3) % 360;
        button.style.backgroundColor = `hsl(${angle}, 100%, 50%)`;
    }, 100);
}

function getMessages(){
}

function postMessage(text){
    
}