const images = document.querySelectorAll('.carousel img');
  const prevBtn = document.getElementById('prev');
  const nextBtn = document.getElementById('next');
  let currentIndex = 0;
  function showImage(index) {
    images.forEach((img, i) => {
      img.classList.toggle('active', i === index);
    });
  }
  prevBtn.addEventListener('click', () => {
    currentIndex = (currentIndex - 1 + images.length) % images.length;
    showImage(currentIndex);
  });
  nextBtn.addEventListener('click', () => {
    currentIndex = (currentIndex + 1) % images.length;
    showImage(currentIndex);
  });
  
  function showRegister(){
        document.getElementById('loginForm').style.display='none';
        document.getElementById('registerForm').style.display='block';
        document.getElementById('message').innerText = '';
      }
   function showLogin(){
        document.getElementById('registerForm').style.display='none';
        document.getElementById('loginForm').style.display='block';
        document.getElementById('message').innerText = '';
      }

      const params = new URLSearchParams(window.location.search);
      const msg = params.get('msg');
      if(msg){
        const messageDiv = document.getElementById('message');
        messageDiv.innerText = decodeURIComponent(msg);
        if(msg.toLowerCase().includes('failed') || msg.toLowerCase().includes('invalid') || msg.toLowerCase().includes('error')){
          messageDiv.style.color = 'red';
        } else {
          messageDiv.style.color = 'green';
        }
      }
	  