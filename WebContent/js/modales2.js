$(document).on('ready', function(){
	on_open_modal();
});

var AJAXRUN = new Array();

function on_open_modal()
{
	$('[id^="modal"]').on('show.bs.modal', function( e ){
		var modal = $(this);
		var boton = $(e.relatedTarget);

		var bts_tipo = boton.attr('bts-tipo');
		var bts_content = boton.attr('bts-content');
		var bts_select_url = modal.find('form').find('select[bts-select-url]');

		var padre = boton.parents('[primary-key]');
		var primary_key = padre.attr('primary-key');
		var bts_mod_url = boton.attr('bts-mod-url');

		var img = padre.find('[bts-img]').attr('src');
		var nombre = padre.find('[bts-nombre]').text();
		var claves = "";


		console.log( "====================================" );
		console.log( "bts_tipo:         " + bts_tipo );
		console.log( "bts_content:      " + bts_content );
		console.log( "bts_select_url.:  " + bts_select_url.length );
		console.log( "primary_key:      " + primary_key );
		console.log( "img:              " + img );
		console.log( "nombre:           " + nombre );
		console.log( "claves:           " + claves );
		console.log( "====================================" );


		/* LLENAR LOS SELECT */
		for(var i=0; i<bts_select_url.length; i++ ){
			var url = $(bts_select_url[i]).attr('bts-select-url');
			var ajax = $.ajax({
				url : url,
				timeout: 15000,
				dataType: 'json',
				async: false,
				success: function( json ){
					llenar_select( json, bts_select_url[i] );
					if( modal.find('input[name="fecha"]').length>0 )
						modal.find('input[name="fecha"]').val( json['fecha'] );
				},
				error: function( a,b,c ){
					console.log("modales2.js: ERROR EN LLENAR LOS SELECT "+c);
					var json = {cant:3, 0:'clave1,nombre1', 1:'clave2,nombre2', 2:'clave3,nombre3'};
					llenar_select( json, bts_select_url[i] );
				}
			});
			AJAXRUN.push( ajax );
		}


		/* PARA EL MODAL DE MODIFICAR */
		if( bts_tipo == "mod" ){
			iniciar_ajax_modificar( bts_mod_url, 'primary_key='+primary_key, 'json', modal, bts_tipo );

		}else if( bts_tipo == "del" ){
			if( bts_content == "tabla" ){
				modal_datos_tabla( modal );
			}else{
				modal_datos_notabla( modal );
			}

		}


		$(modal).find('form').on('submit', function( e ){
			e.preventDefault();

			var ajax = $(this).ajaxSubmit({
				timeout: 15000,
				dataType: 'text',
				data: { primary_key:primary_key },
				target: '#form-server-msg',
				beforeSend: function(){
					$(modal).find('form').find('#form-server-msg').html( 'Espera un cacho....' );
				},
				success: function( text ){
					if( text == "exito" ){
						window.location.reload();
					}
					$('[data-toggle="popover2"]').popover('hide');
					$(modal).find('form').find('#form-server-msg').html( text );
				},
				error: function( s,status,msg ){
					$(modal).find('form').find('#form-server-msg').html( msg );
				}
			});
			AJAXRUN.push( ajax );
		});
		

	});

	$('#modal-borrar').on('hide.bs.modal', function (event) {
		$('[data-toggle="popover1"]').popover('hide');
	});
}













/* POR DEFECTO */
function llenar_select( json, bts_select_url )
{
	var text_option = "";
	console.log( json );

	for( var j=0; j<json.cant; j++ ){
		var clave_valor = json[''+j+''].split(',');
		text_option += "<option value='"+clave_valor[0]+"'> "+clave_valor[1]+" </option>";
	}
	$(bts_select_url).html( text_option );
}











/* MODIFICAR */
function iniciar_ajax_modificar( url, parametros, dataType, modal, tipo )
{
	var ajax = $.ajax({
		url : url,
		timeout: 15000,
		dataType: dataType,
		data: parametros,
		success: function( json ){
			llenar_modal( json,modal );
			modal.find('input[name="imagen"]').removeAttr('required');
		},
		error: function(){
			var json = {nombre:'NOMBRE',descripcion:'DESCRIPCION',hola:'HOLA'};
			llenar_modal( json,modal );
		}
	});
	AJAXRUN.push( ajax );
}


function llenar_modal( json, modal )
{
	var formu = modal.find('form');
	var names = formu.find('[name]');
	var mod_roles = formu.find('input[type="checkbox"][primary-key]');

	for(var i=0;i<names.length;i++){
		var name = $(names[i]).attr('name');

		if( json[name] != null ) {
			var control = formu.find('[name='+name+']');
			if( control.length != 0 ){
				$(control).val( json[name] );
			}else{
				formu.find('[bts-name='+name+']').val( json[name] );
			}
			
		}
	}

	if( json.roles != null ){
		console.log("HAY ROLES PARA MODIFICAR");
		var rols = json.roles.split(';');
		for(var i=0; i<rols.length-1;i++){//EL ULTIMO ES VACIO
			$('input[type="checkbox"][primary-key='+rols[i]+']','#modal-modificar').attr( 'checked','' );
			$('input[type="checkbox"][primary-key='+rols[i]+']','#modal-modificar').parent().addClass( 'active' );
		}
		// PARA MODAL MODIFICAR DE USUARIOS
		if( formu.find('select[name="roles"]') != null ){
			for(var i=0; i<rols.length-1;i++){//EL ULTIMO ES VACIO
				formu.find('select[name="roles"]').find('option[value='+rols[i]+']').prop( 'selected', true );
			}
		}
		
	}


}













/* BORRAR */
function modal_datos_tabla( modal )
{
	var seleccionados = $('input[bts-borrar]');
	var claves = "";
	modal.find('tbody').empty();

	seleccionados.each( function( e ) {
		var elem = $(this);

		if( elem.prop('checked') ){
			var padre = elem.parents('[primary-key]').clone();
			var pk = padre.attr('primary-key');

			padre.find('a').parent().remove();
			padre.find('[bts-borrar]').parent().remove();
			padre.appendTo(modal.find('tbody'));

			claves += pk+";";
		}

	});

	var form = formular( modal, claves );

	modal.find('.modal-footer').find('[data-toggle="popover1"]').attr('data-content',form);
}



function modal_datos_notabla( modal )
{
	var seleccionados = $('input[bts-borrar]');
	var modal_body = "<div class='row'>";
	var claves = "";
	var ind = 0;
	modal.find('.modal-body').empty();


	seleccionados.each( function( e ) {
		var elem = $(this);

		if( elem.prop('checked') ){
			elem.parent().addClass('active');
			var padre = elem.parents('[primary-key]').clone();
			var img = padre.find('[bts-img]').attr('src');
			var nombre = padre.find('[bts-nombre]').text();
			var pk = padre.attr('primary-key');
			claves += pk+";";
			ind += 1;

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
	});

	$(modal_body).appendTo( modal.find('.modal-body') );

	var form = formular( modal, claves );

	modal.find('.modal-footer').find('[data-toggle="popover1"]').attr('data-content',form);
}




function formular( modal, claves )
{
	var url = modal.find('[bts-action]').attr('bts-action');
	if ( url == null ) url = '#';

	var form = 
		"<form action='"+url+"' method='GET'>"+
			"<input type='hidden' name='pks' value='"+ claves +"'>"+
			"<input class='btn btn-link' type='submit' value='Si'>"+
			"<button class='btn btn-link pull-right' data-dismiss='modal'>No</button>"+
		"</form>";

	return form;
}




function cancelarAllAjax()
{
	for(var i=0; i<AJAXRUN.length;i++ ){
		if( AJAXRUN[i] != null ) AJAXRUN[i].abort();
	}
}