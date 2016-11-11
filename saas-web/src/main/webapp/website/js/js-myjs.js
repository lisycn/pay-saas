//getByteLength(str,rule)   setStyle(obj,json)     getByClass(oParent,sClass)
//getPos(obj)  $(fn)             addMouseWheel(obj,fn)    delCookie(name) 
//addCookie(name,value,iDay)     getCookie(name)         getStyle(obj,name)  
//startMove(obj,json,options)

//rule utf8  gb2312
(function(){
     window.getByteLength = function(str,rule){
          var count = 0;    
          //1  取字符串的字符  for
          for(var i = 0; i < str.length; i++){
               // 2 做判断 
               if(str.charAt(i) >= "\u4e00" && str.charAt(i) <= "\u9fa5"){
                    count += 3;
               } else {
                    count++;     
               }
          }   
          return count;
     }
})()

function setStyle(obj,json){
     for(var name in json){
          obj.style[name] = json[name];
     }
}

function findInarr(arr,name){
     for( var i =0; i<arr.length; i++){
          if( arr[i] == name){
               return true;
          }
     }
     return false;
}
function getByClass(oParent,sClass){
     if(oParent.getElementsByClass){
          return oParent.getElementsByClass(sClass);
     }else{
          var aEle = oParent.getElementsByTagName('*');
          var ret = [];
          for(var i = 0; i < aEle.length; i++){
               var tmp = aEle[i].className.split(' ');
               if(findInarr(tmp,sClass)){
                    ret.push(aEle[i]);
               }
          }
          return ret;
     }
}

function getPos(obj){
     var l = 0; 
     var t = 0;

     while(obj){
          l += obj.offsetLeft;
          t += obj.offsetTop;
          obj = obj.offsetParent;
     }

     return {left:l,top:t};
}

//function addReady(fn){  


function addMouseWheel(obj,fn){
     if(window.navigator.userAgent.toLowerCase().indexOf("firefox") != -1){
          //ff 独有
          obj.addEventListener("DOMMouseScroll",fnWheel,false);
     } else {
          // ie chrome
          obj.onmousewheel = fnWheel;
     }

     function fnWheel(ev){        
          var oEvent = ev || event;
          var bDown = true;       
          if(oEvent.wheelDelta){
               bDown = oEvent.wheelDelta > 0 ? false : true;     
          } else {
               bDown = oEvent.detail > 0 ? true : false;
          }
          fn(bDown);         
          oEvent.preventDefault && oEvent.preventDefault();
          return false;              
     }
}


function delCookie(name){
     addCookie(name,1,-1)
}

function addCookie(name,value,iDay){
     iDay = iDay || 0;
     var oDate = new Date();
     oDate.setDate(oDate.getDate() + iDay);
     document.cookie = name + "=" + value + ";expires=" + oDate;
}

function getCookie(name){
     var arr = document.cookie.split("; ");
     for(var i = 0; i < arr.length; i++){
          var arr2 = arr[i].split("=");
          if(arr2[0] == name){
               return arr2[1];
          }
     }
     return "";
}

function getStyle(obj,name){
     return obj.currentStyle ? obj.currentStyle[name] : getComputedStyle(obj,false)[name];
}
(function(){
window.startMove = function(obj,json,options){
     options = options || {};
     options.time = options.time || 700;
     options.type = options.type || 'linear';

     var start = {};
     var dis = {};

     for(var name in json){
          if(name == 'opacity'){
               start[name] = parseFloat(getStyle(obj,name));
          }else{
               start[name] = parseInt(getStyle(obj,name));
          }
          dis[name] = json[name] - start[name];
     }

     var count = Math.round(options.time/30);
     var n = 0;

     clearInterval(obj.timer);
     obj.timer = setInterval(function(){
          n++;
          
          for(var name in json){
               switch(options.type){
                    case 'linear':
                         var a = n/count;
                         var cur = start[name] + dis[name]*a;
                         break;
                    case 'ease-in':
                         var a = n/count;
                         var cur = start[name] + dis[name]*a*a*a;
                         break;
                    case 'ease-out':
                         var a = 1-n/count;
                         var cur = start[name] + dis[name]*(1-a*a*a);
                         break;
               }
               if(name == 'opacity'){
                    obj.style.opacity = cur;
                    obj.style.filter = 'alpha(opacity:'+ cur*100 +')';
               }else{
                    obj.style[name] = cur + 'px';
               }

               if(n ==count){
                    clearInterval(obj.timer);
                    options.fnEnd && options.fnEnd();
               }
          }
     },30);
}
})();

var Tween={Linear:function(t,b,c,d){return c*t/d+b},Quad:{easeIn:function(t,b,c,d){return c*(t/=d)*t+b},easeOut:function(t,b,c,d){return -c*(t/=d)*(t-2)+b},easeInOut:function(t,b,c,d){if((t/=d/2)<1){return c/2*t*t+b}return -c/2*((--t)*(t-2)-1)+b}},Cubic:{easeIn:function(t,b,c,d){return c*(t/=d)*t*t+b},easeOut:function(t,b,c,d){return c*((t=t/d-1)*t*t+1)+b},easeInOut:function(t,b,c,d){if((t/=d/2)<1){return c/2*t*t*t+b}return c/2*((t-=2)*t*t+2)+b}},Quart:{easeIn:function(t,b,c,d){return c*(t/=d)*t*t*t+b},easeOut:function(t,b,c,d){return -c*((t=t/d-1)*t*t*t-1)+b},easeInOut:function(t,b,c,d){if((t/=d/2)<1){return c/2*t*t*t*t+b}return -c/2*((t-=2)*t*t*t-2)+b}},Quint:{easeIn:function(t,b,c,d){return c*(t/=d)*t*t*t*t+b},easeOut:function(t,b,c,d){return c*((t=t/d-1)*t*t*t*t+1)+b},easeInOut:function(t,b,c,d){if((t/=d/2)<1){return c/2*t*t*t*t*t+b}return c/2*((t-=2)*t*t*t*t+2)+b}},Sine:{easeIn:function(t,b,c,d){return -c*Math.cos(t/d*(Math.PI/2))+c+b},easeOut:function(t,b,c,d){return c*Math.sin(t/d*(Math.PI/2))+b},easeInOut:function(t,b,c,d){return -c/2*(Math.cos(Math.PI*t/d)-1)+b}},Expo:{easeIn:function(t,b,c,d){return(t==0)?b:c*Math.pow(2,10*(t/d-1))+b},easeOut:function(t,b,c,d){return(t==d)?b+c:c*(-Math.pow(2,-10*t/d)+1)+b},easeInOut:function(t,b,c,d){if(t==0){return b}if(t==d){return b+c}if((t/=d/2)<1){return c/2*Math.pow(2,10*(t-1))+b}return c/2*(-Math.pow(2,-10*--t)+2)+b}},Circ:{easeIn:function(t,b,c,d){return -c*(Math.sqrt(1-(t/=d)*t)-1)+b},easeOut:function(t,b,c,d){return c*Math.sqrt(1-(t=t/d-1)*t)+b},easeInOut:function(t,b,c,d){if((t/=d/2)<1){return -c/2*(Math.sqrt(1-t*t)-1)+b}return c/2*(Math.sqrt(1-(t-=2)*t)+1)+b}},Elastic:{easeIn:function(t,b,c,d,a,p){if(t==0){return b}if((t/=d)==1){return b+c}if(!p){p=d*0.3}if(!a||a<Math.abs(c)){a=c;var s=p/4}else{var s=p/(2*Math.PI)*Math.asin(c/a)}return -(a*Math.pow(2,10*(t-=1))*Math.sin((t*d-s)*(2*Math.PI)/p))+b},easeOut:function(t,b,c,d,a,p){if(t==0){return b}if((t/=d)==1){return b+c}if(!p){p=d*0.3}if(!a||a<Math.abs(c)){a=c;var s=p/4}else{var s=p/(2*Math.PI)*Math.asin(c/a)}return(a*Math.pow(2,-10*t)*Math.sin((t*d-s)*(2*Math.PI)/p)+c+b)},easeInOut:function(t,b,c,d,a,p){if(t==0){return b}if((t/=d/2)==2){return b+c}if(!p){p=d*(0.3*1.5)}if(!a||a<Math.abs(c)){a=c;var s=p/4}else{var s=p/(2*Math.PI)*Math.asin(c/a)}if(t<1){return -0.5*(a*Math.pow(2,10*(t-=1))*Math.sin((t*d-s)*(2*Math.PI)/p))+b}return a*Math.pow(2,-10*(t-=1))*Math.sin((t*d-s)*(2*Math.PI)/p)*0.5+c+b}},Back:{easeIn:function(t,b,c,d,s){if(s==undefined){s=1.70158}return c*(t/=d)*t*((s+1)*t-s)+b},easeOut:function(t,b,c,d,s){if(s==undefined){s=1.70158}return c*((t=t/d-1)*t*((s+1)*t+s)+1)+b},easeInOut:function(t,b,c,d,s){if(s==undefined){s=1.70158}if((t/=d/2)<1){return c/2*(t*t*(((s*=(1.525))+1)*t-s))+b}return c/2*((t-=2)*t*(((s*=(1.525))+1)*t+s)+2)+b}},Bounce:{easeIn:function(t,b,c,d){return c-Tween.Bounce.easeOut(d-t,0,c,d)+b},easeOut:function(t,b,c,d){if((t/=d)<(1/2.75)){return c*(7.5625*t*t)+b}else{if(t<(2/2.75)){return c*(7.5625*(t-=(1.5/2.75))*t+0.75)+b}else{if(t<(2.5/2.75)){return c*(7.5625*(t-=(2.25/2.75))*t+0.9375)+b}else{return c*(7.5625*(t-=(2.625/2.75))*t+0.984375)+b}}}},easeInOut:function(t,b,c,d){if(t<d/2){return Tween.Bounce.easeIn(t*2,0,c,d)*0.5+b}else{return Tween.Bounce.easeOut(t*2-d,0,c,d)*0.5+c*0.5+b}}}};
;(function(){
     function getStyle(obj,name){
          return (obj.currentStyle || getComputedStyle(obj,false))[name];
     }
     window.doMove=function(obj,json,options){
          clearInterval(obj.timer);
          options=options || {};
          options.time=options.time || 700;
          options.type=options.type || Tween.Elastic.easeOut;
          var count=Math.floor(options.time/20);
          var start={};
          var dis={};
          for(var name in json){
               start[name]=parseFloat(getStyle(obj,name));
               dis[name]=json[name]-start[name];
          }
          
          var n=0;
          obj.timer=setInterval(function(){
               n++;
               
               for(var name in json){
                    var cur=options.type(n/count*options.time,start[name],dis[name],options.time);
                    
                    if(name=='opacity'){
                         obj.style.opacity=cur;
                         obj.style.filter='alpha(opacity:'+cur*100+')';    
                    }else{
                         obj.style[name]=cur+'px';
                    }
               }
               
               if(n==count){
                    clearInterval(obj.timer);
                    options.fnEnd && options.fnEnd(); 
               }
          },20);
     };   
})();

function drag(obj){
     obj.onmousedown = function(ev){
          var oEvent = ev || event;
          var disX = oEvent.clientX - obj.offsetLeft;
          var disY = oEvent.clientY - obj.offsetTop;
          document.onmousemove = function(ev){
               var oEvent = ev || event;
               var l = oEvent.clientX - disX;
               var t = oEvent.clientY - disY;
               obj.style.left = l + 'px';
               obj.style.top  = t + 'px';
          };
          document.onmouseup = function(){
               document.onmousemove = null;
               document.onmouseup = null;
               obj.releaseCapture && obj.releaseCapture();
          };
          obj.setCapture && obj.setCapture();
          return false;
     };
}

function jsonp(json){
     json = json || {};
     if(!json.url){
          alert('URL error'); 
          return;
     }
     json.data = json.data || {};
     json.cbName = json.cbName || 'cb';
     var fnName = 'show'+Math.random();
     fnName = fnName.replace('.','');
     window[fnName] = function(data){
          json.fnSucc&&json.fnSucc(data);    
          oHead.removeChild(oS);
     }
     var arr = [];
     json.data[json.cbName] = fnName;
     for(var name in json.data){
          arr.push(name+'='+json.data[name]);
     }
     
     var oS = document.createElement('script');
     oS.src=json.url+'?'+arr.join('&');
     var oHead = document.getElementsByTagName('head')[0];
     oHead.appendChild(oS);
}

function json2url(json){
     json.t = Math.random();
     var arr = [];
     for(var name in json){
          arr.push(name +'='+ json[name]);
     }
     return arr.join('&');
}
function ajax(json){
     json = json || {};
     if(!json.url){
          alert('error');
          return;
     }
     json.type = json.type || 'GET';
     json.data = json.data || {};
     json.time = json.time || 3000;
     json.dataType = json.dataType || 'text';

     if(window.XMLHttpRequest){
          var oAjax = new XMLHttpRequest();
     }else{
          var oAjax = new ActiveXObject('Microsoft.XMLHTTP');
     }

     switch(json.type.toLowerCase()){
          case 'get':
               oAjax.open('GET',json.url+'?'+ json2url(json.data),true);
               oAjax.send();
               break;
          case 'post':
               oAjax.open('POST',json.url,true);
               oAjax.setRequestHeader('Content-Type','application/x-www-form-urlencoded');
               oAjax.send(json2url(json.data));
               break;
     }

     json.loading && json.loading();

     oAjax.onreadystatechange = function(){
          if(oAjax.readyState == 4){
               clearInterval(timer);
               if(oAjax.status >= 200 && oAjax.status <300 || oAjax.status == 304){
                    if(json.dataType.toLowerCase() == 'xml'){
                         json.success && json.success(oAjax.responseXML);
                    }else{
                         json.success && json.success(oAjax.responseText);
                    }
               }else{
                    json.error && json.error(oAjax.status);
               }
               json.complete && json.complete();
          }
     };

     var timer = null;
     timer = setInterval(function(){
          alert('网络不给力');
          oAjax.onreadystatechange=null;
     },json.time);
}