
( function( factory ){
	if (typeof jQuery === 'undefined') throw '>>> modalajax.js: REQUIERE DE "jquery v2" O SUPERIOR PARA FUNCIONAR';
	if("function"!=typeof jQuery.fn.ajaxSubmit) throw '>>> modalajax.js: REQUIERE DE "jquery.form v3.5" O SUPERIOR PARA FUNCIONAR';
	if("function"!=typeof jQuery.fn.modal) throw '>>> modalajax.js: REQUIERE DE "bootstrap modal v3" O SUPERIOR PARA FUNCIONAR';
	factory( jQuery );

}( function( $ ){

	$.fn.modalajax3 = function(opciones){

		var self = {
			debug: false, // para mostrar errores (no usado)
			showPks: false, // para mostrar pks alt/del en el servermsg
			loadFormDataType: 'json',// otro valor se carga una vista
			loadFormDataTarget: '.modal-body', // donde cargar los datos del servidor si no es json
			pk: '[bts-pk]', // botones dentro un padre
			pksToServer: 'pks', // claves enviadas al server alt/del
			onEventLoad: 'show.bs.modal', // evento para activar este plugin
			onEventClose: 'hide.bs.modal', // evento para cuando para el cierre
			servermsg: '[bts-servermsg]', // para mensajes
			action: '[action]', // url al submit
			getaction: '[bts-getaction]', // url al abrir
			selectdata: '[bts-selectdata]', // para llenar un select
			inputdata: '[bts-inputdata]', // para llenar un input sin name
			nochangevalue: '[value]', // no cambiar el valor

			imagen: '[bts-imagen]', // si content != tabla
			nombre: '[bts-nombre]', // si content != tabla
			check: '[bts-borrar]', // check seleccionados para borrar/alta
			
			content_tipo: '[bts-content-tipo]', // dentro del modal indicando:bts-content-tipo="tabla" o otro valor
			tipo: '[bts-tipo]', // dentro del modal indicando:bts-tipo="del" o bts-tipo="alt"
			contend: '[bts-tabla]', // tabla o mantener otro estilo NOTA: el nombre content daba error asi que se llamo contend
			contenthide: '[bts-contenthide]',
			content_put: '[bts-content-put]', // significa: el contend poner en content_put
			
			buttons: '[bts-buttons]', // botones de submit y cancelar
			button_submit: '[bts-button-submit]', // solo boton de submit
			button_cancel: '[bts-button-cancel]', // solo boton de cancelar
			onLoader: function( elemento,btn ){}, // cuando se ejecuta el onEventLoad
			onClosed: function( elemento ){}, // cuando se ejecuta el onEventClose
			onBefore: function( elemento ){}, // antes de submit el form
			onSuccess : function( elemento,json ){}, // success del submit form
			onError: function( a,b,c ){}, // error del submit form
			onComplete: function(){}, // completo de submit (no usado)
			onGetBefore: function( elemento ){}, // antes del getaction
			onGetSuccess: function( elemento,json ){}, // success del getaction
			onGetSuccessTerminated: function( elemento,json ){}, // success del getaction luego de cargar los datos
			onGetError: function( a,b,c ){}, // error del getaction
			msg_beforeSend: '<span class="text-warning"> esperando respuesta... </span>',
			msg_noJsonResponse: '<p class="text-danger"> el servidor debe responder con json{exito:bool,reload:bool,msg:string} </p>',
			msg_jsonResponseReload: '<p class="text-success"> exito, recargando... </p>',
		};
		$.extend(self, opciones);






		function attrname( atributo ){
			return atributo.replace('[','').replace(']','');
		}

		function getAttrNamePadre( padre, elemento, atributo){
			var attr = atributo.replace('[','').replace(']','');
			return $( padre ).find( elemento ).attr( attr );
		}

		function serverMsg( padre,msg ){
			$( padre ).find( self.servermsg ).html( msg );
		}

		function serverMsgAppend( padre,msg ){
			$( padre ).find( self.servermsg ).append( msg );
		}

		function isCambiarValor( elemento ){
			var attr = attrname( self.nochangevalue );
			attr = $( elemento ).attr( attr );
			//console.log( attr );
			// cambiado para la maquina thania
			return (attr != null || attr.length !=0 );
		}


		


		function cargarGetAction2( MODAL,json ){
			var names = $(MODAL).find('[name]'),
			cambiar_valor = "";
			tag = "",
			type = "",
			name = "",
			valor = "",
			select_data = "",
			input_data = "",
			se_ = attrname( self.selectdata ),
			in_ = attrname( self.inputdata );
			
			names.each(function(i){
				tag = $(names[i]);
				type = names[i].type || names[i].tagName;
				name = tag.attr( 'name' );
				select_data = tag.attr( se_ ) || name;
				input_data = tag.attr( in_ ) || name;
				valor = json[name] || json[select_data] || json[input_data];

				//console.log( name + "    " + type + "    " + valor + "    " +select_data + "     " + input_data);
				type = type.toLowerCase();
				cambiar_valor = isCambiarValor( tag );
				if( cambiar_valor ){
					switch( type ){
						case 'radio':
							console.log( 'falta marcar radio' );
							break;
						case 'checkbox':
							console.log( 'falta marcar checkbox' );
							break;
						case 'select':
	                	case 'select-one':
						case 'select-multiple':
							// clave1,valor1;clave2,valor2
							var valores = json[select_data].split(';');
							valor = new String(valor).split(';');
							tag.empty();

							for( var j=0;j<valores.length;j++ ){
								var pareja = valores[j].split(',');
								tag.append('<option value='+pareja[0]+'>'+pareja[1]+'</option>');
							}
							console.log( '>> '+valor );
							tag.val( valor );
							break;
						case 'text':
						case 'hidden':
						case 'textarea':
							tag.val( valor );
							break;
						default:
							tag.val( valor );
						break;
					}
				}
			});
		}




		function ajaxgetAction( MODAL,url,pk,dataType ){
			self.onGetBefore( MODAL );
			$.ajax({
				url: url,
				timeout: 30000,
				data: { pk: pk },
				dataType: dataType,
				beforeSend: function(){
					serverMsg( MODAL,self.msg_beforeSend );
				},
				success: function( json ){
					self.onGetSuccess( MODAL,json );
					serverMsg( MODAL,'' );
					if( self.loadFormDataType == 'json' ) {
						cargarGetAction2( MODAL,json );
					}
					else {
						$(MODAL).find( self.loadFormDataTarget ).html(json);
					}
					self.onGetSuccessTerminated( MODAL,json );
				},
				error: function( a,b,c ){
					serverMsg( MODAL,'<span class="text-danger text-center">ERROR: en el getaction="'+url+'"</span>'+a.responseText );
					self.onGetError( a,b,c );
				}
			});
		}




		function action_submit( form,MODAL,PK ){
			self.onBefore( MODAL );// aqui funciona bien
			$( form ).ajaxSubmit({
				timeout: 30000,
				dataType: 'json',// si o si json
				data: { pk:PK },
				beforeSend: function(){
					serverMsg( MODAL,'<p class="text-info"> enviando solicitud... </p>' );
				},
				uploadProgress: function( evento,position,total,percent ){
					serverMsg( MODAL,'<p class="text-info"> '+percent+'% </p>' );
				},
				success: function( json ){
					//json{exito:bool,reload:bool,msg:string}
					self.onSuccess( MODAL,json );
					console.log( json );
					if( json == null ) serverMsg( MODAL,self.msg_noJsonResponse );
					else if( !json['reload'] ){
						if( json['exito'] ){
							serverMsg( MODAL,'<p class="text-success"> '+json['msg']+' </p>' );
						}else{
							serverMsg( MODAL,'<p class="text-danger"> '+json['msg']+' </p>' );
						}
					}else{
						serverMsg( MODAL,'<p class="text-success"> '+self.msg_jsonResponseReload+' </p>' );
						window.location.reload();
					}				
				},
				error: function( s,status,msg ){
					
					serverMsg( MODAL,'<span class="text-danger text-center">ERROR: '+status+' :: '+msg+'</span>'+self.msg_noJsonResponse+' '+s.responseText );
					self.onError( s,status,msg );
				}
			});
		}






		function contentAltDel( MODAL,CONTENT,TIPO ){
			var cantidad = $( self.check,self.contend ).length;
			if( cantidad == 0 ) serverMsg( MODAL,'<p class="text-danger">defina los checked a borrar con el atributo: '+self.check+', dentro de '+self.contend+'</p>' );
			else{
				var claves = "",
				checkeds = $( self.check,self.contend ),
				contenedor = "";

				var content_put = $( MODAL ).find( self.content_put );
				if( content_put.length == 0 ) serverMsg( MODAL,'<p class="text-danger">defina el: '+self.content_put+' para poner los datos del/alt</p>' );
				else content_put.empty();

				if( CONTENT == "tabla" ){
					for( var i=0;i<checkeds.length;i++ ){
						contenedor = $( checkeds[i] ).parents( self.pk ).clone();
						if( contenedor.length == 0 ) serverMsg( MODAL,'<p class="text-danger">los elementos con atributo '+self.check+' deben estar dentro de otro con atributo '+self.pk+'</p>' );

						if( $(checkeds[i]).prop('checked') ){
							$(checkeds[i]).parent().addClass('active');
							claves += contenedor.attr( attrname( self.pk ) )+";";
							contenedor.find( self.contenthide ).remove(); // eliminamos componentes que no queremos que se vean en el modal

							content_put.append('<tr>'+contenedor.html()+'</tr>');
						}
					}
				}else {
					var modal_body = "<div class='row'>";
					var ind = 0;
					for( var i=0; i<checkeds.length; i++ ){
						contenedor = $( checkeds[i] ).parents( self.pk ).clone();
						if( contenedor.length == 0 ) serverMsg( MODAL,'<p class="text-danger">los elementos con atributo '+self.check+' deben estar dentro de otro con atributo '+self.pk+'</p>' );

						if( $(checkeds[i]).prop('checked') ){
							$(checkeds[i]).parent().addClass('active');
							claves += contenedor.attr( attrname( self.pk ) )+";";
							var img = contenedor.find( self.imagen ).attr('src');
							var nombre = contenedor.find( self.nombre ).text();
							ind += 1;
							//if( img == null ) throw 'ERROR: boton: si [content != "tabla"] entonces defina una imagen con atributo imagen[<img src="RUTA" imagen>] a mostrar dentro del modal #'+elem.attr('id');
							//if( nombre.length == 0 ) throw 'ERROR: boton: si [content != "tabla"] entonces defina un elemento con atributo [nombre] a mostrar dentro del modal #'+elem.attr('id');
							modal_body +=
							"<div class='col-sm-6'>" +
							"<div class='row'>" +
							"<div class='col-xs-4'>" +
							"<img class='img-responsive' src="+img+">" +
							"</div>" +
							"<div class='col-xs-8'>" +
							"<p class='text-info' style='overflow-x: auto;'>"+nombre+"</p>" +
							"</div>" +
							"</div>" +
							"<p class='visible-xs-block'></p>" +
							"</div>";

							if( ind % 2 == 0) modal_body= modal_body + "</div> <p></p> <div class='row'>";

						}
					}
					$(modal_body).appendTo( content_put );
				}

				if( claves.length > 0 ) claves = claves.substring( 0,claves.length-1 );
				else {
					serverMsg( MODAL,'<p class="text-danger">No hay elementos seleccionados</p>' );
					$( MODAL ).find( self.buttons ).empty();
				}
				if( self.showPks ) serverMsgAppend( MODAL,'<p class="text-info">claves: '+claves+'</p>' );

				if( claves.length > 0 && content_put.length > 0 ){
					var action = $( MODAL ).find( self.action ).attr( attrname( self.action ) );
					var formu = 
					"<form action='"+action+"' method='POST'>"+
					"<input type='hidden' name='"+self.pksToServer+"' value='"+ claves +"'>"+
					"<input class='btn btn-link' type='submit' value='Si'>"+
					"<button class='btn btn-link pull-right' data-dismiss='modal'>No</button>"+
					"</form>";

					var popover = "";
					if( TIPO == "del" ) popover = '<a class="btn btn-danger center-block" data-toggle="modalpopover">Borrar</a>';
					else if( TIPO == "alt" ) popover = '<a class="btn btn-success center-block" data-toggle="modalpopover">Dar alta</a>';
					MODAL.find( self.buttons ).empty();
					MODAL.find( self.buttons ).append( popover );
					MODAL.find( self.buttons ).find('[data-toggle="modalpopover"]').popover({
						placement: 'top',
						title:'¿Estas seguro?',
						html: true,
						content: $(formu)
					});

					var hayButtons = MODAL.find( self.buttons );
					if( hayButtons.length > 0 ){
						hayButtons.find('[data-toggle="modalpopover"]').on('shown.bs.popover', function () {
							hayButtons.find('.popover').find('form').on('submit', function(e){
								hayButtons.find('[data-toggle="modalpopover"]').popover('hide');
								action_submit( this,MODAL,null );
								return false;
							});
						});
					}else{
						// no probado tal ves no funque
						// NOTA: enviar las claves como pks
						MODAL.find('form').on('submit', function(e){
							action_submit( this,MODAL,null );
							return false;
						});
					}
				}
			}
		}

		



		return this.each( function(){
			var elem = $(this);

			$(elem).on( self.onEventLoad, function(e){
				self.onLoader( elem,$(e.relatedTarget) );

				var MODAL = elem;
				var BOTON = $(e.relatedTarget);
				var tipo = $(MODAL).find( self.tipo );
				var content_tipo = $(MODAL).find( self.content_tipo );
				var action = $(MODAL).find( self.action );
				var getaction = $(MODAL).find( self.getaction );
				var PK = BOTON.attr( attrname( self.pk ) ); // se busca en el propio boton

				if( tipo.length > 0 ) tipo = getAttrNamePadre( MODAL,self.tipo,self.tipo );
				if( content_tipo.length > 0 ) content_tipo = getAttrNamePadre( MODAL,self.content_tipo,self.content_tipo );
				if( action.length > 0 ) action = getAttrNamePadre( MODAL,self.action,self.action );
				if( getaction.length > 0 ) getaction = getAttrNamePadre( MODAL,self.getaction,self.getaction );
				if( PK == null ) PK = BOTON.parents( self.pk ).attr( attrname( self.pk ) ); // buscamos en los padres del boton				
				
				

				/*console.log("==========================================");
				console.log(self.pk+": 	         		"+PK);
				console.log(self.getaction+": 			"+getaction);
				console.log(self.action+": 				"+action);
				console.log(self.tipo+": 				"+tipo);
				console.log(self.content_tipo+": 		"+content_tipo);
				console.log(self.contend+": 			"+self.contend);
				console.log("loadFormDataType: 			"+self.loadFormDataType);
				console.log("loadFormDataTarget: 		"+self.loadFormDataTarget);
				console.log("==========================================");*/

				$(MODAL).find( self.servermsg ).empty();

				
				if( tipo.length > 0 ) {
					contentAltDel( MODAL,content_tipo,tipo );
				}else {
					if( getaction.length > 0 ) ajaxgetAction( MODAL,getaction,PK,self.loadFormDataType );
				}

				$( MODAL ).find( self.action ).on('submit',function(e){
					action_submit( this,MODAL,PK );
					return false;
				});
			});


			$(elem).on( self.onEventClose, function(e){
				self.onClosed( elem );
				$(elem).find( self.servermsg ).empty();
				$(this).find('form').unbind('submit');
			});		

		});
	};

}));