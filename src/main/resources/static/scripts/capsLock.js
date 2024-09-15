function testCapsLock(e) {
    if (e.getModifierState('CapsLock')) {
        document.getElementById("caps-lock").style.display = "block";
    } else {
        document.getElementById("caps-lock").style.display = "none";
    }
}

document.addEventListener('keyup', testCapsLock);
