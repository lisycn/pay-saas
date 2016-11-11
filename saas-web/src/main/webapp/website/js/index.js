$(function(){
	var aSection = getByClass(document,'section');
     var oDotBox = document.getElementById('dot');
     var aDot = oDotBox.getElementsByTagName('li');
     var aDotSlide = oDotBox.getElementsByTagName('div');
     var iNow = 0;
     var h = 0;
     var aPos = [];
     
     var str = window.location.hash;
     if(str){
          iNow = str.substring(1);
          navPoint(iNow);
     }
	
     //设置高度
     setHeight();
     function setHeight(){
          for(var i = 0; i < aSection.length; i++){
               var h = document.documentElement.clientHeight;
               aSection[i].style.height = h + 'px';
               //aPos.push(getPos(aSection[i]));
          }
     }
    
     //滚轮滚动切换
     (function(){
		function mouseWheel(){
               setHeight();
               addMouseWheel(document,function(bDown){
                    if(bDown){
                         iNow++;
                         if(iNow == aSection.length)iNow=aSection.length-1;
                    }else{
                         iNow--;
                         if(iNow < 0)iNow=0;
                    }        
                    navPoint(iNow);
                    move(iNow);

                  
                    window.location.hash = iNow;
                    
               });
     	}
     	window.onresize = window.onscroll = setHeight;
		mouseWheel();
     })();  

     //dot点击切换
     (function(){
          for(var i = 0; i < aDot.length; i++){
               (function(index){
                    aDot[i].onclick = function(){
                         iNow = index;          
                         navPoint(iNow)
                         move(iNow);

                        
                         window.location.hash = iNow;
                    }
               })(i);
          }
     })();

  
     //dot ---> active 函数
     function navPoint(iNow){
          for(var i=0;i<aDot.length;i++){
               aDot[i].className = '';
          }
          aDot[iNow].className = 'active';
     }

     //鼠标滑过dot slide
     (function(){
          for(var i = 0; i < aDot.length; i++){
               (function(index){
                    aDot[i].onmousemove = function(){
                         startMove(aDotSlide[index],{width:94},{time:200});
                    }
                    aDot[i].onmouseout = function(){
                         startMove(aDotSlide[index],{width:0},{time:200});
                    }
               })(i);
          }
     })();

     //scrollTop 运动框架
     var timer=null;
     function move(iNow){
		 var iTarget = iNow * $('.section').height();
          var count=Math.floor(1000/30);      
          var start=document.documentElement.scrollTop || document.body.scrollTop;      
          var dis=iTarget-start;
          var n=0;
          clearInterval(timer);
          timer=setInterval(function(){
               n++;        
               var a=1-n/count;
               var cur=start+dis*(1-Math.pow(a,3));          
               document.documentElement.scrollTop=document.body.scrollTop=cur;                           
               if(n==count){
                    clearInterval(timer);    
               }
          },30);
     };


});